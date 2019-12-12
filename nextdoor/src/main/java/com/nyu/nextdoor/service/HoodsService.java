package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.HoodsMapper;
import com.nyu.nextdoor.model.Blocks;
import com.nyu.nextdoor.model.Hoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HoodsService {
    private HoodsMapper hoodsMapper;
    private BlocksServices blocksServices;

    @Autowired
    HoodsService(HoodsMapper hoodsMapper, BlocksServices blocksServices) {
        this.hoodsMapper = hoodsMapper;
        this.blocksServices = blocksServices;
    }

    public Integer getUserHoods(int userId) {
        Integer blocksId = blocksServices.getUserBlocks(userId);
        if(blocksId == null) {
            return null;
        }

        Blocks blocks = blocksServices.getBlocksById(blocksId);
        if(blocks == null) {
            return null;
        }

        Hoods hoods = getHoodsById(blocks.getHoodsId());
        if(hoods == null) {
            return null;
        }

        return hoods.getHoodsId();

    }

    public Hoods getHoodsById(int hoodsId) {
        return hoodsMapper.getHoodsById(hoodsId);
    }

    public List<Integer> getUserIdsInHoods(int hoodsId) {
        List<Blocks> blocksList = blocksServices.getBlocksByHoodsId(hoodsId);
        List<Integer> userIdsInHoods = new ArrayList<Integer>();
        for(Blocks blocks: blocksList) {
            List<Integer> userIds = blocksServices.getUserIdsInBlocks(blocks.getBlocksId());
            userIdsInHoods.addAll(userIds);
        }
        return userIdsInHoods;
    }

    public boolean checkUserInHoods(int userId, int hoodsId) {
        return getUserHoods(userId) == hoodsId;
    }
}
