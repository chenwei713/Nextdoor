package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Friends;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendsMapper {
    @Select("SELECT user1_id as userId1, user2_id as userId2 " +
            "FROM nextdoor.friends " +
            "WHERE user1_id = #{userId} OR user2_id = #{userId}")
    List<Friends> getFriendsList(Integer userId);

    @Insert("INSERT `nextdoor`.`friends` " +
            "(`user1_id`, `user2_id`) " +
            "VALUES " +
            "(#{userId1}, #{userId2})")
    void addFriends(Integer userId1, Integer userId2);
}
