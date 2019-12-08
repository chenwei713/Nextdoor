package com.nyu.nextdoor.model;

import java.sql.Timestamp;

public class Threads {
    private Integer userId;
    private Integer threadsId;
    private String subject;
    private String content;
    private Timestamp timestamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getThreadsId() {
        return threadsId;
    }

    public void setThreadsId(Integer threadsId) {
        this.threadsId = threadsId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
