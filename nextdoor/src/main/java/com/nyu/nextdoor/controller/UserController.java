package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;
    private AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
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
    public Object getUser(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            // TODO: MD5 password
            user.setPassword("XXXXX");
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
            user.setUserId(userId);
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
