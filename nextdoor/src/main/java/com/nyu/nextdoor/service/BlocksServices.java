package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.BlocksApplicationMapper;
import com.nyu.nextdoor.mapper.BlocksMapper;
import com.nyu.nextdoor.model.Blocks;
import com.nyu.nextdoor.model.BlocksApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlocksServices {
    private BlocksMapper blocksMapper;
    private BlocksApplicationMapper blocksApplicationMapper;

    @Autowired
    BlocksServices(BlocksMapper blocksMapper, BlocksApplicationMapper blocksApplicationMapper) {
        this.blocksMapper = blocksMapper;
        this.blocksApplicationMapper = blocksApplicationMapper;
    }

    public List<Blocks> getAllBlocks() {
        return blocksMapper.getAllBlocks();
    }

    public void addInBlocks(int userId, int blocksId) {
        this.blocksMapper.addInBlocks(userId, blocksId);
    }

    public void updateBlocksApplication(BlocksApplication blocksApplication) {
        this.blocksApplicationMapper.updateBlocksApplication(blocksApplication);
    }

    public List<Integer> getUserIdsInBlocks(int blocksId) {
        return blocksMapper.getUserIdsInBlocks(blocksId);
    }

    public void applyBlocks(int userId, int blocksId) {
        blocksApplicationMapper.addNewApplication(userId, blocksId);
    }

    public boolean checkUserInBlocks(int userId, int blocksId) {
        Integer userBlocksId = getUserBlocks(userId);
        if(userBlocksId == null) return false;
        return userBlocksId == blocksId;
    }

    public Integer getUserBlocks(int userId) {
        return blocksMapper.getUserBlocks(userId);
    }

    public Blocks getBlocksById(int blocksId) {
        return blocksMapper.getBlocksById(blocksId);
    }

    public List<BlocksApplication> getBlocksApplications(int blocksId) {
        return blocksApplicationMapper.getBlocksApplications(blocksId);
    }

    public BlocksApplication getBlocksApplication(int blocksApplicationId) {
        return blocksApplicationMapper.getBlocksApplication(blocksApplicationId);
    }



    public List<Blocks> getBlocksByHoodsId(int hoodsId) {
        return blocksMapper.getBlocksByHoodsId(hoodsId);
    }

}
