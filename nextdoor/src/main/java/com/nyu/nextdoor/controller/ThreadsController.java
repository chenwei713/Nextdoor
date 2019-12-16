package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.*;
import com.nyu.nextdoor.service.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadsController {
    private UserService userService;
    private ThreadsService threadsService;
    private SharingListService sharingListService;
    private BlocksServices blocksServices;
    private HoodsService hoodsService;
    private AuthenticationService authenticationService;

    ThreadsController(UserService userService, ThreadsService threadsService,
                      AuthenticationService authenticationService, SharingListService sharingListService,
                      BlocksServices blocksServices, HoodsService hoodsService) {
        this.userService = userService;
        this.threadsService = threadsService;
        this.sharingListService = sharingListService;
        this.blocksServices = blocksServices;
        this.hoodsService = hoodsService;
        this.authenticationService = authenticationService;
    }

    /*
    *   POST: Create threads and config privacy, update unread table
    * */
    @CheckLogin
    @PostMapping("")
    public Object addThreads(@RequestHeader(value = "token") String token,
                             @RequestBody ThreadsRequest threadsRequest) throws IOException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Threads threads = threadsRequest.getThreads();
        List<SharingList> sharingList = threadsRequest.getSharingList();

        threads.setUserId(user.getUserId());

        String threadsId = UUID.randomUUID().toString();
        threads.setThreadsId(threadsId);

        if(threadsRequest.getImageBase64() != null) {
            // Use constant later
            String folder = "/Users/xiechenwei/Desktop/NYU_2019Fall/github_nextdoor/data/";
            String[] base64Image = threadsRequest.getImageBase64().split(",");
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image[1]);
            String imageUrl = folder + threadsId + ".jpeg";
            File file = new File(imageUrl);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(imageBytes);
            threads.setImageUrl(imageUrl);
        }
        threadsService.addThreads(threads);

        // Update read table and sharing list table in this function
        threadsService.updateAllRecords(sharingList, threadsId, user.getUserId());

        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    *   Get threads
    * */
    @CheckLogin
    @GetMapping("/{threads_id}")
    public  Object getThreads(@PathVariable("threads_id") String threadsId,
                              @RequestHeader(value = "token") String token) throws IOException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(!threadsService.checkThreadsRight(threadsId, user.getUserId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Threads threads = threadsService.getThreadsById(threadsId);
        if(threads == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(threads.getImageUrl() != null) {
            File file =  new File(threads.getImageUrl());
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            String base64 = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            threads.setImageUrl(base64);
        }

        HashMap<String, Object> response = new HashMap<>();
        List<Message> messages = threadsService.getCommentsByThreadsId(threadsId);
        response.put("threads", threads);
        response.put("messages", messages);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    *   Post messages under threads
    * */
    @CheckLogin
    @PostMapping("/comments")
    public Object addMessage(@RequestHeader(value = "token") String token,
                             @RequestBody Message message) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        message.setUserId(user.getUserId());
        if(threadsService.checkThreadsRight(message.getThreadsId(), user.getUserId())) {
            threadsService.addMessage(message);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<SharingList> sharingLists = sharingListService.getSharingLists(message.getThreadsId());
        threadsService.updateUnreadRecords(sharingLists, message.getThreadsId());

        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    *   Friends Feeds: Get threads posted by friends
    * */
    @CheckLogin
    @GetMapping("/feeds/friends")
    public Object getFriendsFeeds(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<String> resultList = new ArrayList<>();
        int limit = 20;
        int offset = 0;
        while(resultList.size() < 10 && offset < 1000) {
            List<String> threadsIdList = threadsService.getLimitedUnReadThreadsIds(user.getUserId(), limit, offset);
            if(threadsIdList.size() == 0) {
                break;
            }
            for(String threadsId: threadsIdList) {
                if(threadsService.checkFriendsThreads(threadsId, user.getUserId())) {
                    if(resultList.size() < 10) {
                        resultList.add(threadsId);
                    }
                }
            }
            offset += 20;
        }

        for(String threadsId: resultList) {
            threadsService.deleteUnreadRecord(threadsId, user.getUserId());
        }

        if(resultList.size() < 10) {
            List<String> threadsIdList = sharingListService.getSharingThreadsByUserId(user.getUserId());
            for(String threadsId: threadsIdList) {
                if(resultList.size() < 10 && threadsService.checkFriendsThreads(threadsId, user.getUserId())
                        && !resultList.contains(threadsId)) {
                    resultList.add(threadsId);
                }
            }
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /*
     *   Neighbors Feeds: Get threads posted by neighbors
     * */
    @CheckLogin
    @GetMapping("/feeds/neighbors")
    public Object getNeighborsFeeds(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<String> resultList = new ArrayList<>();
        int limit = 20;
        int offset = 0;
        while(resultList.size() < 10 && offset < 1000) {
            List<String> threadsIdList = threadsService.getLimitedUnReadThreadsIds(user.getUserId(), limit, offset);
            if(threadsIdList.size() == 0) {
                break;
            }
            for(String threadsId: threadsIdList) {
                if(threadsService.checkNeighborsThreads(threadsId, user.getUserId())) {
                    if(resultList.size() < 10) {
                        resultList.add(threadsId);
                    }
                }
            }
            offset += 20;
        }

        for(String threadsId: resultList) {
            threadsService.deleteUnreadRecord(threadsId, user.getUserId());
        }

        if(resultList.size() < 10) {
            List<String> threadsIdList = sharingListService.getSharingThreadsByUserId(user.getUserId());
            for(String threadsId: threadsIdList) {
                if(resultList.size() < 10 && threadsService.checkNeighborsThreads(threadsId, user.getUserId())
                        && !resultList.contains(threadsId)) {
                    resultList.add(threadsId);
                }
            }
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }


    /*
     *   Blocks Feeds: Get threads posted in Blocks
     * */
    @CheckLogin
    @GetMapping("/feeds/blocks")
    public Object getBlocksFeeds(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<String> resultList = new ArrayList<>();
        int limit = 20;
        int offset = 0;
        while(resultList.size() < 10 && offset < 1000) {
            List<String> threadsIdList = threadsService.getLimitedUnReadThreadsIds(user.getUserId(), limit, offset);
            if(threadsIdList.size() == 0) {
                break;
            }
            for(String threadsId: threadsIdList) {
                if(threadsService.checkBlocksThreads(threadsId, user.getUserId())) {
                    if(resultList.size() < 10) {
                        resultList.add(threadsId);
                    }
                }
            }
            offset += 20;
        }

        for(String threadsId: resultList) {
            threadsService.deleteUnreadRecord(threadsId, user.getUserId());
        }

        if(resultList.size() < 10) {
            List<String> threadsIdList = sharingListService.getSharingThreadsByUserId(user.getUserId());
            for(String threadsId: threadsIdList) {
                if(resultList.size() < 10 && threadsService.checkBlocksThreads(threadsId, user.getUserId())
                        && !resultList.contains(threadsId)) {
                    resultList.add(threadsId);
                }
            }
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }

    /*
     *   Hoods Feeds: Get threads posted in Hoods
     * */
    @CheckLogin
    @GetMapping("/feeds/hoods")
    public Object getHoodsFeeds(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<String> resultList = new ArrayList<>();
        int limit = 20;
        int offset = 0;
        while(resultList.size() < 10 && offset < 1000) {
            List<String> threadsIdList = threadsService.getLimitedUnReadThreadsIds(user.getUserId(), limit, offset);
            if(threadsIdList.size() == 0) {
                break;
            }
            for(String threadsId: threadsIdList) {
                if(threadsService.checkHoodsThreads(threadsId, user.getUserId())) {
                    if(resultList.size() < 10) {
                        resultList.add(threadsId);
                    }
                }
            }
            offset += 20;
        }

        for(String threadsId: resultList) {
            threadsService.deleteUnreadRecord(threadsId, user.getUserId());
        }

        if(resultList.size() < 10) {
            List<String> threadsIdList = sharingListService.getSharingThreadsByUserId(user.getUserId());
            for(String threadsId: threadsIdList) {
                if(resultList.size() < 10 && threadsService.checkHoodsThreads(threadsId, user.getUserId())
                        && !resultList.contains(threadsId)) {
                    resultList.add(threadsId);
                }
            }
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }

    /*
     *   Key words Feeds: Get threads by key words
     * */
    @CheckLogin
    @GetMapping("/feeds/search/{keyWords}")
    public Object getSearchFeeds(@RequestHeader(value = "token") String token,
                                @PathVariable("keyWords") String keyWords) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<String> searchedThreads = threadsService.searchThreadsByKeyWords(keyWords);
        List<String> readableTreads = new ArrayList<>();

        for(String threadsId: searchedThreads) {
            if(threadsService.checkThreadsRight(threadsId, user.getUserId())) {
                readableTreads.add(threadsId);
            }
        }
        return new ResponseEntity<>(readableTreads, HttpStatus.OK);
    }




}
