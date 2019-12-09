package com.nyu.nextdoor.model;

import java.sql.Timestamp;

public class BlocksApplication {
    private Integer userId;
    private Integer blocksId;
    private Integer applicationId;
    private Timestamp timestamp;
    private Integer isApproved;
    private Integer isValid;
    private Integer approval1;
    private Integer approval2;
    private Integer approval3;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBlocksId() {
        return blocksId;
    }

    public void setBlocksId(Integer blocksId) {
        this.blocksId = blocksId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Integer getApproval1() {
        return approval1;
    }

    public void setApproval1(Integer approval1) {
        this.approval1 = approval1;
    }

    public Integer getApproval2() {
        return approval2;
    }

    public void setApproval2(Integer approval2) {
        this.approval2 = approval2;
    }

    public Integer getApproval3() {
        return approval3;
    }

    public void setApproval3(Integer approval3) {
        this.approval3 = approval3;
    }
}
