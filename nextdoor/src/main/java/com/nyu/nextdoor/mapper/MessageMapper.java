package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("INSERT `nextdoor`.`message` " +
            "(`user_id`, `thread_id`, `parent_message_id`, `content`) " +
            "VALUES" +
            "(#{userId}, #{threadsId}, #{parentMessageId}, #{content})")
    void addMessage(Message message);

    @Select("SELECT user_id as userId, message_id as messageId, thread_id as threadsId, " +
            "parent_message_id as parentMessageId, content " +
            "FROM nextdoor.message " +
            "WHERE thread_id = #{threadsId}")
    List<Message> getCommentsByThreadsId(String threadsId);
}
