package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.UserMapper;
import com.nyu.nextdoor.model.Setting;
import com.nyu.nextdoor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void addNewUser(User user) {
            userMapper.addNewUser(user);
    }

    public void addSetting(int userId) {
        userMapper.addSetting(userId);
    }

    public void updateSetting(Setting setting) {
        userMapper.updateSetting(setting);
    }

    public User getUserByID(int id) {
        return userMapper.getUserByID(id);
    }

    public User getUserByAccountName(String AccountName) {
        User user = new User();
        user.setAccountName(AccountName);
        return userMapper.getUserByAccountName(user);
    }

    public void updateUserInfo(User user) {
        userMapper.updateUserInfo(user);
    }

    public void updateUserAddress(User user) {
        userMapper.updateUserAddress(user);
    }

    public void updatePhoto(Integer userId, String userPhotoUrl) {
        userMapper.updateUserPhoto(userId, userPhotoUrl);
    }

    public String getImageBase64(User user) throws IOException {
        String imageUrl = user.getUserPhotoUrl();
        InputStream file = new FileInputStream(imageUrl);
        byte[] data = new byte[file.available()];
        file.read(data);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}
