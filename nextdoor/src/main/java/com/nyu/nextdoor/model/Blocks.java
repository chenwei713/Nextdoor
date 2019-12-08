package com.nyu.nextdoor.model;

public class Blocks {
    private Integer blocksId;
    private String blocksName;
    private String blocksDescription;
    private Double longitude1;
    private Double latitude1;
    private Double longitude2;
    private Double latitude2;

    public Integer getBlocksId() {
        return blocksId;
    }

    public void setBlocksId(Integer blocksId) {
        this.blocksId = blocksId;
    }

    public String getBlocksName() {
        return blocksName;
    }

    public void setBlocksName(String blocksName) {
        this.blocksName = blocksName;
    }

    public String getBlocksDescription() {
        return blocksDescription;
    }

    public void setBlocksDescription(String blocksDescription) {
        this.blocksDescription = blocksDescription;
    }

    public Double getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(Double longitude1) {
        this.longitude1 = longitude1;
    }

    public Double getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(Double latitude1) {
        this.latitude1 = latitude1;
    }

    public Double getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(Double longitude2) {
        this.longitude2 = longitude2;
    }

    public Double getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(Double latitude2) {
        this.latitude2 = latitude2;
    }
}
