package com.nyu.nextdoor.mapper;

import com.nyu.nextdoor.model.Hoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HoodsMapper {
    @Select("SELECT hoods_id as hoodsId, hoods_name as hoodsName, hoods_description as hoodsDescription, " +
            "longitude1, latitude1, longitude2, latitude2 " +
            "FROM nextdoor.Hoods " +
            "WHERE hoods_id = #{hoodsId}")
    Hoods getHoodsById(Integer hoodsId);

}
