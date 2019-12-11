package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.model.LoginUser;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /*
    *   Get token or Sign in
    *   TODO: Token should be expired every half an hour
    * */
    @PostMapping("")
    public Object getToken(@RequestBody LoginUser user) {
        try {
            User userByAccountName = userService.getUserByAccountName(user.getAccountName());
            if(userByAccountName == null || !userByAccountName.getPassword().equals(user.getPassword())) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                String token = authenticationService.getToken(user);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return new ResponseEntity<Map>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
