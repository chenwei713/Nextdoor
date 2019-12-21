package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.FriendsApplicationMapper;
import com.nyu.nextdoor.mapper.FriendsMapper;
import com.nyu.nextdoor.model.Friends;
import com.nyu.nextdoor.model.FriendsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsService {
    private FriendsMapper friendsMapper;
    private FriendsApplicationMapper friendsApplicationMapper;

    @Autowired
    FriendsService(FriendsMapper friendsMapper, FriendsApplicationMapper friendsApplicationMapper) {
        this.friendsMapper = friendsMapper;
        this.friendsApplicationMapper = friendsApplicationMapper;
    }

    public List<Friends> getFriendsList(int userId) {
        return this.friendsMapper.getFriendsList(userId);
    }

    public void applyFriends(FriendsApplication friendsApplication) {
        this.friendsApplicationMapper.addNewApplication(friendsApplication);
    }

    public FriendsApplication getFriendsApplicationById(int friendsApplicationId) {
        return this.friendsApplicationMapper.getFriendsApplicationById(friendsApplicationId);
    }

    public List<FriendsApplication> getSentApplicationList(int userId) {
        return this.friendsApplicationMapper.getSentApplicationList(userId);
    }

    public List<FriendsApplication> getReceivedApplicationList(int userId) {
        return this.friendsApplicationMapper.getReceivedApplicationList(userId);
    }

    public void approveFriendsApplication(FriendsApplication friendsApplication) {
        this.friendsApplicationMapper.approveApplication(friendsApplication);
    }

    public void declineFriendsApplication(FriendsApplication friendsApplication) {
        this.friendsApplicationMapper.declineApplication(friendsApplication);
    }

    public void addFriends(int userId1, int userId2) {
        this.friendsMapper.addFriends(userId1, userId2);
    }

    public boolean checkFriends(int userId1, int userId2) {
        Friends friends = this.friendsMapper.getFriends(userId1, userId2);
        return !(friends == null);
    }

    public void deleteFriends(int userId1, int userId2) {
        this.friendsMapper.deleteFriends(userId1, userId2);
    }

    public void deleteAllFriends(int userId) {
        friendsMapper.deleteAllFriends(userId);
    }

}
