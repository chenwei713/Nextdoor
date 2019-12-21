package com.nyu.nextdoor.model;

import java.sql.Timestamp;

public class Message {
    private Integer userId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String threadsId;
    private Integer messageId;
    private Integer parentMessageId;
    private String content;
    private Timestamp timestamp;

    public String getThreadsId() {
        return threadsId;
    }

    public void setThreadsId(String threadsId) {
        this.threadsId = threadsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(Integer parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
