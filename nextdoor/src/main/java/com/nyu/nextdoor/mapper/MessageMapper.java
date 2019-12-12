package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    @Insert("INSERT `nextdoor`.`message` " +
            "(`user_id`, `thread_id`, `parent_message_id`, `content`) " +
            "VALUES" +
            "(#{userId}, #{threadsId}, #{parentMessageId}, #{content})")
    void addMessage(Message message);
}
