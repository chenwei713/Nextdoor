package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signup")
public class SignUpController {
    private UserService userService;

    @Autowired
    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    /*
    *   New user sign up
    * */
    @PostMapping("")
    public Object signup(@RequestBody User user) {
        // Check duplicate account name in advance
        if(userService.getUserByAccountName(user.getAccountName()) != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            userService.addNewUser(user);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
