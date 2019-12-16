package com.nyu.nextdoor.model;

import java.util.List;

public class ThreadsRequest {
    private Threads threads;
    String imageBase64;

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

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
