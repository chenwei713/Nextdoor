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

        List<Integer> neighborsList = neighborsService.getNeighborsList(user.getUserId());

        return new ResponseEntity<>(neighborsList, HttpStatus.OK);
    }

    @CheckLogin
    @PostMapping("/follow")
    public Object follow(@RequestHeader(value = "token") String token,
                         @RequestBody Neighbors neighbors) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User targetNeighbor = userService.getUserByID(neighbors.getUserId2());
        if(targetNeighbor == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        neighbors.setUserId1(user.getUserId());

        neighborsService.follow(neighbors);

        return new ResponseEntity(HttpStatus.OK);
    }


}
