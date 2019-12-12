package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.SharingList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SharingListMapper {
    @Insert("INSERT `nextdoor`.`sharing_list` " +
            "(`thread_id`, `sharing_user_id`) " +
            "VALUES " +
            "(#{threadsId}, #{userId})")
    void addSharingList(SharingList sharingList);

    @Select("SELECT sharing_user_id " +
            "FROM nextdoor.sharing_list " +
            "WHERE thread_id = #{threadsId}")
    List<Integer> getSharingUsers(String threadsId);

    @Select("SELECT thread_id as threadsId, sharing_user_id as userId " +
            "FROM nextdoor.sharing_list " +
            "WHERE thread_id = #{threadsId}")
    List<SharingList> getSharingLists(String threadsId);

    @Select("SELECT thread_id as threadsId " +
            "FROM nextdoor.sharing_list " +
            "WHERE sharing_user_id = #{userId} " +
            "ORDER BY timestamp DESC ")
    List<String> getSharingThreadsByUserId(Integer userId);
}
