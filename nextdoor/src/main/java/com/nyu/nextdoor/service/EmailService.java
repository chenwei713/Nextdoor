package com.nyu.nextdoor.service;

import com.nyu.nextdoor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    private UserService userService;

    @Autowired
    public EmailService(UserService userService) {
        this.userService = userService;
    }

    @Async
    public void sendEmail(Integer autherId, List<Integer> receiverList) {
        User auther = userService.getUserByID(autherId);
        List<String> receiverEmailList = new ArrayList<>();
        for(Integer userId: receiverList) {
            User receiver = userService.getUserByID(userId);
            receiverEmailList.add(receiver.getAccountName());
        }

        for(String email: receiverEmailList) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("nextdoor2019@sina.com");
            msg.setTo(email);
            msg.setCc("nextdoor2019@sina.com");
            msg.setSubject("New Message from: " + auther.getAccountName());
            msg.setText("Hello from Next door! New posts!");
            javaMailSender.send(msg);
        }
    }
}
