package com.nyu.nextdoor.model;

public class Setting {
    private Integer userId;
    private Integer emailFriendsFeed;
    private Integer emailNeighborsFeed;
    private Integer emailBlocksFeed;
    private Integer emailHoodsFeed;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEmailFriendsFeed() {
        return emailFriendsFeed;
    }

    public void setEmailFriendsFeed(Integer emailFriendsFeed) {
        this.emailFriendsFeed = emailFriendsFeed;
    }

    public Integer getEmailNeighborsFeed() {
        return emailNeighborsFeed;
    }

    public void setEmailNeighborsFeed(Integer emailNeighborsFeed) {
        this.emailNeighborsFeed = emailNeighborsFeed;
    }

    public Integer getEmailBlocksFeed() {
        return emailBlocksFeed;
    }

    public void setEmailBlocksFeed(Integer emailBlocksFeed) {
        this.emailBlocksFeed = emailBlocksFeed;
    }

    public Integer getEmailHoodsFeed() {
        return emailHoodsFeed;
    }

    public void setEmailHoodsFeed(Integer emailHoodsFeed) {
        this.emailHoodsFeed = emailHoodsFeed;
    }
}
