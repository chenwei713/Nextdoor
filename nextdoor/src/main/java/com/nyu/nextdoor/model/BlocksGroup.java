package com.nyu.nextdoor.model;

public class BlocksGroup {
    private Integer blocksId;
    private Integer threadsNum;
    private Double longitude;
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getBlocksId() {
        return blocksId;
    }

    public void setBlocksId(Integer blocksId) {
        this.blocksId = blocksId;
    }

    public Integer getThreadsNum() {
        return threadsNum;
    }

    public void setThreadsNum(Integer threadsNum) {
        this.threadsNum = threadsNum;
    }
}
