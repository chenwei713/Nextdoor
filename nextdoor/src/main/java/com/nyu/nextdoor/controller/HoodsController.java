package com.nyu.nextdoor.controller;

import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.HoodsService;
import com.nyu.nextdoor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hoods")
public class HoodsController {
    private UserService userService;
    private HoodsService hoodsServices;
    private AuthenticationService authenticationService;

    public HoodsController(UserService userService, HoodsService hoodsService,
                           AuthenticationService authenticationService) {
        this.userService = userService;
        this.hoodsServices = hoodsService;
        this.authenticationService = authenticationService;
    }

    /*
    *   Get all users in Hoods
    * */
    @CheckLogin
    @GetMapping("")
    public Object getPeopleList(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Integer hoodsId = hoodsServices.getUserHoods(user.getUserId());
        if(hoodsId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<Integer> userIdsInHoods = hoodsServices.getUserIdsInHoods(hoodsId);
        List<User> userInHoods = new ArrayList<>();
        for(Integer i: userIdsInHoods) {
            userInHoods.add(userService.getUserByID(i));
        }
        return new ResponseEntity<>(userInHoods, HttpStatus.OK);
    }

}
