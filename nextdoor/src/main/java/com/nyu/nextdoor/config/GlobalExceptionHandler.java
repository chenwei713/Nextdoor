package com.nyu.nextdoor.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    //private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        //logger.error(e.getStackTrace().toString());
        String msg = e.getMessage();
        Map<String, String> response = new HashMap<>();
        response.put("Message", msg);
        if(e instanceof AccessDeniedException) {
            return new ResponseEntity<Map>(response, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<Map>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
