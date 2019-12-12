package com.nyu.nextdoor.model;

import java.util.List;

public class ThreadsRequest {
    private Threads threads;
    private List<SharingList> sharingList;

    public Threads getThreads() {
        return threads;
    }

    public void setThreads(Threads threads) {
        this.threads = threads;
    }

    public List<SharingList> getSharingList() {
        return sharingList;
    }

    public void setSharingList(List<SharingList> sharingList) {
        this.sharingList = sharingList;
    }
}
