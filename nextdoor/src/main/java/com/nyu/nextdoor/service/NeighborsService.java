package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.NeighborsMapper;
import com.nyu.nextdoor.model.Neighbors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeighborsService {
    private NeighborsMapper neighborsMapper;

    @Autowired
    NeighborsService(NeighborsMapper neighborsMapper) {
        this.neighborsMapper = neighborsMapper;
    }

    public void follow(Neighbors neighbors) {
        this.neighborsMapper.follow(neighbors);
    }

    public List<Integer> getNeighborsList(int userId) {
        return this.neighborsMapper.getNeighborsList(userId);
    }

    public boolean checkNeighbors(int userId1, int userId2) {
        Neighbors neighbors = this.neighborsMapper.getNeighbors(userId1, userId2);
        return neighbors != null;
    }


}
