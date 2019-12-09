package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Blocks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    Integer getUserBlocks(int userId);
}
