package com.nyu.nextdoor.model;

import java.sql.Timestamp;

public class SharingList {
    private String threadsId;
    private Integer userId;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

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
}
