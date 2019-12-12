package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.SharingListMapper;
import com.nyu.nextdoor.model.SharingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharingListService {
    private SharingListMapper sharingListMapper;

    @Autowired
    SharingListService(SharingListMapper sharingListMapper) {
        this.sharingListMapper = sharingListMapper;
    }

    public void addSharingList(SharingList sharingList) {
        this.sharingListMapper.addSharingList(sharingList);
    }

    public List<Integer> getSharingUsers(String threadsId) {
        return this.sharingListMapper.getSharingUsers(threadsId);
    }

    public List<SharingList> getSharingLists(String threadsId) {
        return this.sharingListMapper.getSharingLists(threadsId);
    }

    public List<String> getSharingThreadsByUserId(int userId) {
        return this.sharingListMapper.getSharingThreadsByUserId(userId);
    }
}
