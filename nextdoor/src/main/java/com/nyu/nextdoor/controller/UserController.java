package com.nyu.nextdoor.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // This API is for test. Should delete later
    @CheckLogin
    @GetMapping("{id}")
    public Object get(@PathVariable("id") int userId) {
        User getUser = userService.getUserByID(userId);
        if(getUser == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            getUser.setPassword("XXXXX");
            return new ResponseEntity<User>(getUser, HttpStatus.OK);
        }
    }

}
