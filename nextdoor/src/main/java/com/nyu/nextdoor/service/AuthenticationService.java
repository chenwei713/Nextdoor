package com.nyu.nextdoor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.nyu.nextdoor.model.LoginUser;
import com.nyu.nextdoor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;

@Service
public class AuthenticationService {
    private UserService userService;

    @Autowired
    AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public String getToken(LoginUser user) throws UnsupportedEncodingException {
        return JWT.create().withAudience(user.getAccountName()).sign(Algorithm.HMAC256(user.getPassword()));
    }

    public User getUserFromToken(String token) throws AccessDeniedException {
        String accountName;

        try {
            accountName = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new AccessDeniedException("Token invalid");
        }

        if(accountName == null) throw new AccessDeniedException("Token invalid");

        return this.userService.getUserByAccountName(accountName);
    }

    public boolean checkToken(String token, int userId) throws AccessDeniedException {
        String accountName;

        if(userService == null) {
            System.out.println("wcnm");
        }

        try {
            accountName = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new AccessDeniedException("Token invalid");
        }

        if(accountName == null) throw new AccessDeniedException("Token invalid");

        User user = this.userService.getUserByAccountName(accountName);
        return user.getUserId() == userId;
    }
}
