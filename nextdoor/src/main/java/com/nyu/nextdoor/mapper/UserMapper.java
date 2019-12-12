package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Setting;
import com.nyu.nextdoor.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT user_id as userId, user_first_name as userFirstName, user_last_name as userLastName, " +
            "user_street as userStreet, user_city as userCity, user_state as userState, longitude, latitude, " +
            "user_profile as userProfile, user_photo_url as userPhotoUrl, account_name as accountName, password," +
            "telephone_number as telephoneNumber " +
            "FROM nextdoor.user " +
            "WHERE user_id = #{userId}")
    User getUserByID(Integer userId);

    @Select("SELECT user_id as userId, user_first_name as userFirstName, user_last_name as userLastName, " +
            "user_street as userStreet, user_city as userCity, user_state as userState, longitude, latitude, " +
            "user_profile as userProfile, user_photo_url as userPhotoUrl, account_name as accountName, password," +
            "telephone_number as telephoneNumber " +
            "FROM nextdoor.user " +
            "WHERE account_name = #{accountName}")
    User getUserByAccountName(User user);

    @Insert("INSERT INTO `nextdoor`.`user` " +
            "(`user_first_name`, `user_last_name`, `user_street`, `user_city`, `user_state`, `longitude`, " +
            "`latitude`, `account_name`, `password`, `telephone_number`)" +
            "VALUES" +
            "(#{userFirstName}, #{userLastName}, #{userStreet}, #{userCity}, #{userState}, #{longitude}," +
            " #{latitude}, #{accountName}, #{password}, #{telephoneNumber})")
    void addNewUser(User user);

    @Insert("INSERT INTO `nextdoor`.`setting` " +
            "(`user_id`) " +
            "VALUES " +
            "(#{userId})")
    void addSetting(Integer userId);

    @Update("UPDATE `nextdoor`.`setting` " +
            "SET " +
            "`email_friends_feed` = #{emailFriendsFeed}, " +
            "`email_neighbors_feed` = #{emailNeighborsFeed}, " +
            "`email_blocks_feed` = #{emailBlocksFeed}, " +
            "`email_hoods_feed` = #{emailHoodsFeed}, " +
            "WHERE user_id = #{userId}")
    void updateSetting(Setting setting);

    @Update("UPDATE `nextdoor`.`user` " +
            "SET" +
            "`user_first_name` = #{userFirstName}, " +
            "`user_last_name` = #{userLastName}, " +
            "`user_street` = #{userStreet}, " +
            "`user_city` = #{userCity}, " +
            "`user_state` = #{userState}, " +
            "`longitude` = #{longitude}, " +
            "`latitude` = #{latitude}, " +
            "`user_profile` = #{userProfile}, " +
            "`user_photo_url` = #{userPhotoUrl}, " +
            "`telephone_number` = #{telephoneNumber} " +
            "WHERE user_id = #{userId}")
    void updateUser(User user);

}
