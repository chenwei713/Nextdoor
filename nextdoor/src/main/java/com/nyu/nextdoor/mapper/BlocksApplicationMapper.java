package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.BlocksApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlocksApplicationMapper {
    @Insert("INSERT INTO `nextdoor`.`blocks_application`" +
            "(`user_id`, `blocks_id`)" +
            "VALUES" +
            "(#{userId}, #{blocksId})")
    void addNewApplication(int userId, int blocksId);

    @Select("SELECT application_id as applicationId, user_id as userId, blocks_id as blocksId," +
            "timestamp as timestamp, is_approved as isApproved, is_valid as isValid," +
            "approval1, approval2, approval2 " +
            "FROM nextdoor.blocks_application " +
            "WHERE blocks_id = #{blocksId}")
    List<BlocksApplication> getBlocksApplications(int blocksId);


}
