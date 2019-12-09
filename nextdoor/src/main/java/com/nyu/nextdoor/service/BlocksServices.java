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

    public List<BlocksApplication> getBlocksApplications(int blocksId) {
        return blocksApplicationMapper.getBlocksApplications(blocksId);
    }
}
