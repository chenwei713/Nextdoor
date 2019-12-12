package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Blocks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BlocksMapper {
    @Select("SELECT blocks_id as blocksId, hoods_id as hoodsId, blocks_name as blocksName, " +
            "blocks_description as blocksDescription, longitude1, latitude1, longitude2, latitude2 " +
            "FROM nextdoor.blocks")
    List<Blocks> getAllBlocks();

    @Select("SELECT blocks_id as blocksId " +
            "FROM nextdoor.in_blocks " +
            "WHERE user_id = #{userId}" )
    Integer getUserBlocks(Integer userId);

    @Select("SELECT blocks_id as blocksId, hoods_id as hoodsId, blocks_name as blocksName, " +
            "blocks_description as blocksDescription, longitude1, latitude1, longitude2, latitude2 " +
            "FROM nextdoor.blocks " +
            "WHERE blocks_id = #{blocksId}")
    Blocks getBlocksById(Integer blocksId);

    @Select("SELECT user_id as userId " +
            "FROM nextdoor.in_blocks " +
            "WHERE blocks_id = #{blocksId}")
    List<Integer> getUserIdsInBlocks(Integer blocksId);

    @Select("SELECT blocks_id as blocksId, hoods_id as hoodsId, blocks_name as blocksName, " +
            "blocks_description as blocksDescription, longitude1, latitude1, longitude2, latitude2 " +
            "FROM nextdoor.blocks " +
            "WHERE hoods_id = #{hoodsId}")
    List<Blocks> getBlocksByHoodsId(Integer hoodsId);


}
