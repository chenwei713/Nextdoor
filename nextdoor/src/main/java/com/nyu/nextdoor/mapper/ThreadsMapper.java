package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Threads;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThreadsMapper {

    @Insert("INSERT `nextdoor`.`thread` " +
            "(`thread_id`, `user_id`,`subject`, `content`, `image_url`) " +
            "VALUES " +
            "(#{threadsId}, #{userId}, #{subject}, #{content}, #{imageUrl})")
    void addThreads(Threads threads);

    @Select("SELECT thread_id as threadsId, user_id as userId, subject, timestamp, content, image_url as imageUrl " +
            "FROM nextdoor.thread " +
            "WHERE thread_id = #{threadsId}")
    Threads getThreadsById(String threadsId);

    @Insert("INSERT `nextdoor`.`read_threads` " +
            "(`user_id`, `thread_id`) " +
            "VALUES " +
            "(#{userId}, #{threadsId})")
    void addUnreadRecords(Integer userId, String threadsId);

    @Select("SELECT thread_id " +
            "FROM nextdoor.read_threads " +
            "WHERE user_id = #{userId} " +
            "ORDER BY timestamp DESC " +
            "LIMIT #{limit} OFFSET #{offset} ")
    List<String> getLimitedUnReadThreadsIds(Integer userId, Integer limit, Integer offset);

    @Delete("DELETE FROM `nextdoor`.`read_threads` " +
            "WHERE user_id = #{userId} AND thread_id = #{threadsId}")
    void deleteUnreadRecord(String threadsId, Integer userId);

    @Select("SELECT thread_id " +
            "FROM nextdoor.thread " +
            "WHERE subject LIKE #{keyWords} OR content LIKE #{keyWords} " +
            "ORDER BY timestamp DESC")
    List<String> searchThreadsByKeyWords(String keyWords);

}
