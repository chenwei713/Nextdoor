package com.nyu.nextdoor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nyu.nextdoor.model.LoginUser;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class AuthenticationService {
    public String getToken(LoginUser user) throws UnsupportedEncodingException {
        return JWT.create().withAudience(user.getAccountName()).sign(Algorithm.HMAC256(user.getPassword()));
    }
}
