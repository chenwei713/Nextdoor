package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.*;
import com.nyu.nextdoor.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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
                             @RequestBody ThreadsRequest threadsRequest) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Threads threads = threadsRequest.getThreads();
        List<SharingList> sharingList = threadsRequest.getSharingList();

        if(!user.getUserId().equals(threads.getUserId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String threadsId = UUID.randomUUID().toString();
        threads.setThreadsId(threadsId);
        threadsService.addThreads(threads);

        // Update read table and sharing list table in this function
        threadsService.updateAllRecords(sharingList, threadsId, user.getUserId());

        return new ResponseEntity(HttpStatus.OK);
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

        if(!user.getUserId().equals(message.getUserId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

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




}
