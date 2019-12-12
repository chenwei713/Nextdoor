package com.nyu.nextdoor.model;

import java.sql.Timestamp;

public class ReadThreads {
    private Integer userId;
    private String threadsId;
    private Timestamp timestamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getThreadsId() {
        return threadsId;
    }

    public void setThreadsId(String threadsId) {
        this.threadsId = threadsId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
