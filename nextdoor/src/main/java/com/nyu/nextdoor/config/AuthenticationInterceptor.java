package com.nyu.nextdoor.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        CheckLogin methodAnnotation = method.getAnnotation(CheckLogin.class);
        // No need to check
        if (methodAnnotation == null) return true;

        String token = request.getHeader("token");

        if (token == null) throw new AccessDeniedException("Token absence");

        String accountName;

        try {
            accountName = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new AccessDeniedException("Token invalid");
        }

        if(accountName == null) throw new AccessDeniedException("Token invalid");

        User user = this.userService.getUserByAccountName(accountName);

        if (user == null) throw new AccessDeniedException("Token invalid");

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new AccessDeniedException("Token Invalid");
        }

        request.setAttribute("currentUser", user);
        return true;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
