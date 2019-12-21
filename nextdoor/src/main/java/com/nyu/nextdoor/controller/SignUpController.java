package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.FriendsService;
import com.nyu.nextdoor.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private FriendsService friendsService;
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SignUpController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }

    /*
    *   New user sign up
    * */
    @PostMapping("")
    public Object signup(@RequestBody User user) {
        if(user.getAccountName() == null) {
            LOGGER.info("AccountName absence");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        // Check duplicate account name in advance
        if(userService.getUserByAccountName(user.getAccountName()) != null) {
            LOGGER.info("Duplicate accountName");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String password = user.getPassword();
        if(password == null) {
            LOGGER.info("Password absence");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        user.setPassword(md5Hex);

        user.setUserPhotoUrl("/Users/xiechenwei/Desktop/d/default.jpg");

        userService.addNewUser(user);

        User userNew = userService.getUserByAccountName(user.getAccountName());
        userService.addSetting(userNew.getUserId());

        return new ResponseEntity(HttpStatus.OK);
    }
}
