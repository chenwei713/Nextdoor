package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.Neighbors;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.NeighborsService;
import com.nyu.nextdoor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/api/v1/neighbors")
public class NeighborsController {
    private NeighborsService neighborsService;
    private UserService userService;
    private AuthenticationService authenticationService;

    NeighborsController(NeighborsService neighborsService, AuthenticationService authenticationService, UserService userService) {
        this.neighborsService = neighborsService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @CheckLogin
    @GetMapping("")
    public Object get(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        List<Integer> neighborsIdsList = neighborsService.getNeighborsList(user.getUserId());
        List<User> neighborsList = new ArrayList<>();
        for(Integer i: neighborsIdsList) {
            neighborsList.add(userService.getUserByID(i));
        }

        return new ResponseEntity<>(neighborsList, HttpStatus.OK);
    }

    @CheckLogin
    @PostMapping("/follow/{target_user_id}")
    public Object follow(@RequestHeader(value = "token") String token,
                         @PathVariable("target_user_id") int target_user_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User targetNeighbor = userService.getUserByID(target_user_id);
        if(targetNeighbor == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(neighborsService.checkNeighbors(user.getUserId(), target_user_id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Neighbors neighbors = new Neighbors();
        neighbors.setUserId1(user.getUserId());
        neighbors.setUserId2(target_user_id);

        neighborsService.follow(neighbors);

        return new ResponseEntity(HttpStatus.OK);
    }

    @CheckLogin
    @PostMapping("/unfollow/{target_user_id}")
    public Object unFollow(@RequestHeader(value = "token") String token,
                         @PathVariable("target_user_id") int target_user_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User targetNeighbor = userService.getUserByID(target_user_id);
        if(targetNeighbor == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(!neighborsService.checkNeighbors(user.getUserId(), target_user_id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Neighbors neighbors = new Neighbors();
        neighbors.setUserId1(user.getUserId());
        neighbors.setUserId2(target_user_id);

        neighborsService.unfollow(neighbors);

        return new ResponseEntity(HttpStatus.OK);
    }

    @CheckLogin
    @GetMapping("/check/{target_id}")
    public Object isFollow(@RequestHeader(value = "token") String token,
                           @PathVariable("target_id") int target_id) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        HashMap<String, Integer> response = new HashMap<>();
        response.put("IsFollow", neighborsService.checkNeighbors(user.getUserId(), target_id)? 1: 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
