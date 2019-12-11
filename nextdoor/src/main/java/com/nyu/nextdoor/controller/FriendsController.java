package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.Friends;
import com.nyu.nextdoor.model.FriendsApplication;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.FriendsService;
import com.nyu.nextdoor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private FriendsService friendsService;
    private AuthenticationService authenticationService;
    private UserService userService;

    FriendsController(FriendsService friendsService, AuthenticationService authenticationService, UserService userService) {
        this.friendsService = friendsService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     *  Get a friends list
     */
    @CheckLogin
    @GetMapping("")
    public Object get(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        List<Friends> friendsList = friendsService.getFriendsList(user.getUserId());
        List<User> friendsUserList = new ArrayList<>();
        for (Friends friend : friendsList) {
            if(!friend.getUserId1().equals(user.getUserId())) {
                friendsUserList.add(userService.getUserByID(friend.getUserId1()));
            } else {
                friendsUserList.add(userService.getUserByID(friend.getUserId2()));
            }
        }
        return new ResponseEntity<>(friendsUserList, HttpStatus.OK);
    }

    /*
    *   Post a new application for friends
    * */
    @CheckLogin
    @PostMapping("/application")
    public Object applyFriends(@RequestHeader(value = "token") String token,
                               @RequestBody FriendsApplication application) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(!application.getUserId1().equals(user.getUserId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User targetFriend = userService.getUserByID(application.getUserId2());
        if(targetFriend == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        friendsService.applyFriends(application);

        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    *   Get received application for friends
    * */
    @CheckLogin
    @GetMapping("/application/received")
    public Object getReceivedFriendsApplication(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<FriendsApplication> friendsApplicationsList = friendsService.getReceivedApplicationList(user.getUserId());
        return new ResponseEntity<>(friendsApplicationsList, HttpStatus.OK);
    }

    /*
    *   Get sent application for friends
    * */
    @CheckLogin
    @GetMapping("/application/sent")
    public Object getSentFriendsApplication(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<FriendsApplication> friendsApplicationsList = friendsService.getSentApplicationList(user.getUserId());
        return new ResponseEntity<>(friendsApplicationsList, HttpStatus.OK);
    }

    /*
    *   Approve a received application
    * */
    @CheckLogin
    @PostMapping("/application/approval")
    public Object approveApplication(@RequestHeader(value = "token") String token,
                                     @RequestBody FriendsApplication application) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(!application.getUserId2().equals(user.getUserId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User sourceFriend = userService.getUserByID(application.getUserId1());
        if(sourceFriend == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        friendsService.approveFriendsApplication(application);
        friendsService.addFriends(application.getUserId1(), application.getUserId2());

        return new ResponseEntity(HttpStatus.OK);
    }

}
