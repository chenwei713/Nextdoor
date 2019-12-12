package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Neighbors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NeighborsMapper {
    @Insert("INSERT `nextdoor`.`neighbors` " +
            "(`user1_id`, `user2_id`) " +
            "VALUES " +
            "(#{userId1}, #{userId2})")
    void follow(Neighbors neighbors);

    @Select("SELECT user2_id " +
            "FROM nextdoor.neighbors " +
            "WHERE user1_id = #{userId}")
    List<Integer> getNeighborsList(Integer userId);

    @Select("SELECT user1_id as userId1, user2_id as userId2 " +
            "FROM nextdoor.neighbors " +
            "WHERE user1_id = #{userId1} AND user2_id = #{userId2}")
    Neighbors getNeighbors(Integer userId1, Integer userId2);
}
