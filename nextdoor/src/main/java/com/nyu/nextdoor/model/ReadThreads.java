package com.nyu.nextdoor.model;

public class ReadThreads {
    private Integer userId;
    private Integer threadsId;
    private Integer hasRead;

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

    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }
}
