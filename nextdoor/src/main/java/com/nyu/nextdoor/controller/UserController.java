package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.Setting;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;
    private AuthenticationService authenticationService;
    private FriendsService friendsService;
    private NeighborsService neighborsService;
    private BlocksServices blocksServices;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService,
                          FriendsService friendsService, BlocksServices blocksServices, NeighborsService neighborsService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.friendsService = friendsService;
        this.blocksServices = blocksServices;
        this.neighborsService = neighborsService;
    }

    // This API is for test. Should delete later
    @CheckLogin
    @GetMapping("{id}")
    public Object get(@PathVariable("id") int userId) {
        User getUser = userService.getUserByID(userId);
        if(getUser == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            getUser.setPassword("XXXXX");
            return new ResponseEntity<>(getUser, HttpStatus.OK);
        }
    }

    /*
    *   Get user info by token
    * */
    @CheckLogin
    @GetMapping("")
    public Object getUser(@RequestHeader(value = "token") String token) throws IOException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            // TODO: MD5 password
            user.setPassword("XXXXX");
            if(user.getUserPhotoUrl() != null) {
                user.setImageBase64(userService.getImageBase64(user));
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /*
    *   Update user info
    * */
    @CheckLogin
    @PostMapping("")
    public Object updateUser(@RequestBody User user,
                             @RequestHeader(value = "token") String token) throws AccessDeniedException {
        User userFromToken = authenticationService.getUserFromToken(token);

        if(userFromToken == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            int userId = userFromToken.getUserId();
            User newUserInfo = new User();
            newUserInfo.setUserId(userId);
            newUserInfo.setUserFirstName(user.getUserFirstName());
            newUserInfo.setUserLastName(user.getUserLastName());
            newUserInfo.setTelephoneNumber(user.getTelephoneNumber());
            newUserInfo.setUserProfile(user.getUserProfile());
            userService.updateUserInfo(newUserInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /*
     *   Update user setting
     * */
    @CheckLogin
    @PostMapping("/setting")
    public Object updateSetting(@RequestBody Setting setting,
                             @RequestHeader(value = "token") String token) throws AccessDeniedException {
        User userFromToken = authenticationService.getUserFromToken(token);

        if(userFromToken == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            setting.setUserId(userFromToken.getUserId());
            userService.updateSetting(setting);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /*
    *   upload profile
    * */
    @CheckLogin
    @PostMapping("/image")
    public Object uploadImage(@RequestParam("file") MultipartFile multipartFile,
                              @RequestHeader(value = "token") String token) throws IOException {
        User user = authenticationService.getUserFromToken(token);
        Path path = Paths.get("/Users/xiechenwei/Desktop/d/" + multipartFile.getOriginalFilename());
        System.out.println(path);
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        userService.updatePhoto(user.getUserId(), path.toString());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /*
    *   Get profile
    * */
    @CheckLogin
    @GetMapping("/image")
    public Object getImage(@RequestHeader(value = "token") String token) throws IOException {
        User user = authenticationService.getUserFromToken(token);
        String imageBase64 = "";
        if(user.getUserPhotoUrl() != null) {
            imageBase64 = userService.getImageBase64(user);
        }


        HashMap<String, String> response = new HashMap<>();
        response.put("image", imageBase64);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    *   drop all
    * */
    @CheckLogin
    @PostMapping("/updateAddress")
    public Object updateAddress(@RequestBody User user,
                                @RequestHeader(value = "token") String token) throws AccessDeniedException {
        User userFromToken = authenticationService.getUserFromToken(token);
        if(userFromToken == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        user.setUserId(userFromToken.getUserId());
        userService.updateUserAddress(user);
        friendsService.deleteAllFriends(userFromToken.getUserId());
        neighborsService.deleteAllNeighbors(userFromToken.getUserId());
        blocksServices.deleteUserBlocks(userFromToken.getUserId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
