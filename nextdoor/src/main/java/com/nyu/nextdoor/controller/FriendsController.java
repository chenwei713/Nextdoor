package com.nyu.nextdoor.controller;

import com.google.gson.Gson;
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
    @PostMapping("/application/{target_id}")
    public Object applyFriends(@RequestHeader(value = "token") String token,
                               @RequestBody FriendsApplication application,
                               @PathVariable("target_id") int target_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        application.setUserId1(user.getUserId());
        application.setUserId2(target_id);

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
        List<HashMap> responses = new ArrayList<>();

        Gson gson = new Gson();
        for(FriendsApplication friendsApplication: friendsApplicationsList) {
            String jsonString = gson.toJson(friendsApplication);
            HashMap response = gson.fromJson(jsonString, HashMap.class);
            response.put("name", userService.getUserByID(friendsApplication.getUserId1()).getUserFirstName());
            responses.add(response);
        }


        return new ResponseEntity<>(responses, HttpStatus.OK);
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
    @PostMapping("/application/approval/{friendsApplicationId}")
    public Object approveApplication(@RequestHeader(value = "token") String token,
                                     @PathVariable("friendsApplicationId") int friendsApplicationId) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        FriendsApplication friendsApplication = friendsService.getFriendsApplicationById(friendsApplicationId);

        User sourceFriend = userService.getUserByID(friendsApplication.getUserId1());
        if(sourceFriend == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        friendsService.approveFriendsApplication(friendsApplication);
        friendsService.addFriends(friendsApplication.getUserId1(), friendsApplication.getUserId2());

        return new ResponseEntity(HttpStatus.OK);
    }


    @CheckLogin
    @PostMapping("/application/decline/{friendsApplicationId}")
    public Object declineApplication(@RequestHeader(value = "token") String token,
                                     @PathVariable("friendsApplicationId") int friendsApplicationId) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        FriendsApplication friendsApplication = friendsService.getFriendsApplicationById(friendsApplicationId);

        User sourceFriend = userService.getUserByID(friendsApplication.getUserId1());
        if(sourceFriend == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        friendsService.declineFriendsApplication(friendsApplication);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    *   Check if two people friends
    * */
    @CheckLogin
    @GetMapping("/check/{target_id}")
    public Object checkFriends(@RequestHeader(value = "token") String token,
                               @PathVariable("target_id") int target_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        HashMap<String, Integer> response = new HashMap<>();
        response.put("IsFriends", friendsService.checkFriends(user.getUserId(), target_id)? 1: 0);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /*
    *   Delete friends
    * */
    @CheckLogin
    @PostMapping("/delete/{target_id}")
    public Object deleteFriends(@RequestHeader(value = "token") String token,
                               @PathVariable("target_id") int target_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);

        if(!friendsService.checkFriends(user.getUserId(), target_id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        friendsService.deleteFriends(user.getUserId(), target_id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
