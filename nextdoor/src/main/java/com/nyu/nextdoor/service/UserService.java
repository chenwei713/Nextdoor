package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.UserMapper;
import com.nyu.nextdoor.model.Setting;
import com.nyu.nextdoor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

}
