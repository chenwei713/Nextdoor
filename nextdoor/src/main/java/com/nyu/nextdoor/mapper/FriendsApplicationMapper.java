package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.FriendsApplication;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface FriendsApplicationMapper {
    @Insert("INSERT INTO `nextdoor`.`friends_application` " +
            "(`user1_id`, `user2_id`, `notes`) " +
            "VALUES " +
            "(#{userId1}, #{userId2}, #{notes})")
    void addNewApplication(FriendsApplication friendsApplication);

    @Update("UPDATE `nextdoor`.`friends_application` " +
            "SET" +
            "`is_approved` = 1 " +
            "WHERE application_id = #{applicationId}")
    void approveApplication(FriendsApplication application);

    @Delete("DELETE FROM `nextdoor`.`friends_application` " +
            "WHERE application_id = #{applicationId}")
    void declineApplication(FriendsApplication application);

    @Select("SELECT application_id as applicationId, user1_id as userId1, user2_id as userId2, timestamp, notes, " +
            "is_approved as isApproved " +
            "FROM nextdoor.friends_application " +
            "WHERE user1_id = #{userId}")
    List<FriendsApplication> getSentApplicationList(Integer userId);

    @Select("SELECT application_id as applicationId, user1_id as userId1, user2_id as userId2, timestamp, notes, " +
            "is_approved as isApproved " +
            "FROM nextdoor.friends_application " +
            "WHERE user2_id = #{userId}")
    List<FriendsApplication> getReceivedApplicationList(Integer userId);

    @Select("SELECT application_id as applicationId, user1_id as userId1, user2_id as userId2, timestamp, notes, " +
            "is_approved as isApproved " +
            "FROM nextdoor.friends_application " +
            "WHERE application_id = #{applicationId}")
    FriendsApplication getFriendsApplicationById(Integer applicationId);
}
