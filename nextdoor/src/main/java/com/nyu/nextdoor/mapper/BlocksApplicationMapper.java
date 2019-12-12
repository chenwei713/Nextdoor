package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.BlocksApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BlocksApplicationMapper {
    @Insert("INSERT INTO `nextdoor`.`blocks_application`" +
            "(`user_id`, `blocks_id`)" +
            "VALUES" +
            "(#{userId}, #{blocksId})")
    void addNewApplication(int userId, int blocksId);

    @Update("UPDATE `nextdoor`.`blocks_application` " +
            "SET " +
            "`approval1` = #{approval1}, " +
            "`approval2` = #{approval2}, " +
            "`approval3` = #{approval3}, " +
            "`is_approved` = #{isApproved} " +
            "WHERE application_id = #{applicationId}")
    void updateBlocksApplication(BlocksApplication application);

    @Select("SELECT application_id as applicationId, user_id as userId, blocks_id as blocksId," +
            "timestamp as timestamp, is_approved as isApproved, is_valid as isValid," +
            "approval1, approval2, approval3 " +
            "FROM nextdoor.blocks_application " +
            "WHERE blocks_id = #{blocksId}")
    List<BlocksApplication> getBlocksApplications(int blocksId);

    @Select("SELECT application_id as applicationId, user_id as userId, blocks_id as blocksId," +
            "timestamp as timestamp, is_approved as isApproved, is_valid as isValid," +
            "approval1, approval2, approval3 " +
            "FROM nextdoor.blocks_application " +
            "WHERE application_id = #{blocksApplicationId}")
    BlocksApplication getBlocksApplication(int blocksApplicationId);


}
