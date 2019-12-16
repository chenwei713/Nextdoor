package com.nyu.nextdoor.service;

import com.nyu.nextdoor.mapper.MessageMapper;
import com.nyu.nextdoor.mapper.ThreadsMapper;
import com.nyu.nextdoor.model.Blocks;
import com.nyu.nextdoor.model.Message;
import com.nyu.nextdoor.model.SharingList;
import com.nyu.nextdoor.model.Threads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadsService {
    private ThreadsMapper threadsMapper;
    private MessageMapper messageMapper;
    private SharingListService sharingListService;
    private BlocksServices blocksServices;
    private HoodsService hoodsService;
    private FriendsService friendsService;
    private NeighborsService neighborsService;
    private EmailService emailService;

    @Autowired
    ThreadsService(ThreadsMapper threadsMapper, MessageMapper messageMapper, SharingListService sharingListService,
                   BlocksServices blocksServices, HoodsService hoodsService, FriendsService friendsService,
                   NeighborsService neighborsService, EmailService emailService) {
        this.threadsMapper = threadsMapper;
        this.messageMapper = messageMapper;
        this.blocksServices = blocksServices;
        this.hoodsService = hoodsService;
        this.friendsService = friendsService;
        this.neighborsService = neighborsService;
        this.sharingListService = sharingListService;
        this.emailService = emailService;
    }

    public void addThreads(Threads threads) {
        this.threadsMapper.addThreads(threads);
    }

    public void addMessage(Message message) {
        this.threadsMapper.deleteUnreadRecord(message.getThreadsId(), message.getUserId());
        this.messageMapper.addMessage(message);
    }

    public Threads getThreadsById(String threadsId) {
        return this.threadsMapper.getThreadsById(threadsId);
    }

    public boolean checkThreadsRight(String threadsId, int userId) {
        List<Integer> sharingUsers = sharingListService.getSharingUsers(threadsId);
        if(sharingUsers.contains(userId)) {
            return true;
        }

        if(sharingUsers.contains(0)) {
            int threadsOwnerId = getThreadsById(threadsId).getUserId();
            return blocksServices.checkUserInBlocks(threadsOwnerId, blocksServices.getUserBlocks(userId));
        }

        if(sharingUsers.contains(1)) {
            int threadsOwnerId = getThreadsById(threadsId).getUserId();
            return hoodsService.checkUserInHoods(threadsOwnerId, hoodsService.getUserHoods(userId));
        }

        return false;
    }

    public void addUnreadRecord(int userId, String threadsId) {
        this.threadsMapper.addUnreadRecords(userId, threadsId);
    }

    public void updateAllRecords(List<SharingList> sharingList, String threadsId, int userId) {

        List<Integer> userIdList;
        // public in blocks
        if(sharingList.get(0).getUserId() == 0) {
            Integer blocksId = blocksServices.getUserBlocks(userId);
            if(blocksId == null) return;
            userIdList = blocksServices.getUserIdsInBlocks(blocksId);
            for(int id: userIdList) {
                addUnreadRecord(id, threadsId);
                SharingList sharing = new SharingList();
                sharing.setThreadsId(threadsId);
                sharing.setUserId(id);
                sharingListService.addSharingList(sharing);
            }
        } else if(sharingList.get(0).getUserId() == 1) {    // public in hoods
            Integer hoodsId = hoodsService.getUserHoods(userId);
            if(hoodsId == null) return;
            userIdList = hoodsService.getUserIdsInHoods(hoodsId);
            for(int id: userIdList) {
                addUnreadRecord(id, threadsId);
                SharingList sharing = new SharingList();
                sharing.setThreadsId(threadsId);
                sharing.setUserId(id);
                sharingListService.addSharingList(sharing);
            }
        } else {
            userIdList = new ArrayList<>();
            for(SharingList sharing: sharingList) {    // target people
                userIdList.add(sharing.getUserId());
                addUnreadRecord(sharing.getUserId(), threadsId);
                sharing.setThreadsId(threadsId);
                sharingListService.addSharingList(sharing);
            }
        }

        // Send email to all
        // emailService.sendEmail(userId, userIdList);



    }

    public void updateUnreadRecords(List<SharingList> sharingList, String threadsId) {
        for(SharingList sharing: sharingList) {
            addUnreadRecord(sharing.getUserId(), threadsId);
        }
    }

    public List<String> getLimitedUnReadThreadsIds(Integer userId, Integer limit, Integer offset) {
        return this.threadsMapper.getLimitedUnReadThreadsIds(userId, limit, offset);
    }

    public void deleteUnreadRecord(String threadsId, int userId) {
        threadsMapper.deleteUnreadRecord(threadsId, userId);
    }

    public boolean checkFriendsThreads(String threadsId, int userId) {
        Threads threads = getThreadsById(threadsId);
        if(threads == null) {
            return false;
        }
        Integer threadsOwnerId = threads.getUserId();
        if(threadsOwnerId == null) {
            return false;
        }
        return friendsService.checkFriends(threadsOwnerId, userId);
    }

    public boolean checkNeighborsThreads(String threadsId, int userId) {
        Threads threads = getThreadsById(threadsId);
        if(threads == null) {
            return false;
        }
        Integer threadsOwnerId = threads.getUserId();
        if(threadsOwnerId == null) {
            return false;
        }
        return neighborsService.checkNeighbors(userId, threadsOwnerId);
    }

    public boolean checkBlocksThreads(String threadsId, int userId) {
        Threads threads = getThreadsById(threadsId);
        if(threads == null) {
            return false;
        }
        Integer threadsOwnerId = threads.getUserId();
        if(threadsOwnerId == null) {
            return false;
        }

        Integer ownerBlocksId = blocksServices.getUserBlocks(threadsOwnerId);
        Integer userBlocksId = blocksServices.getUserBlocks(userId);

        return ownerBlocksId != null && ownerBlocksId.equals(userBlocksId);
    }

    public boolean checkHoodsThreads(String threadsId, int userId) {
        Threads threads = getThreadsById(threadsId);
        if(threads == null) {
            return false;
        }
        Integer threadsOwnerId = threads.getUserId();
        if(threadsOwnerId == null) {
            return false;
        }

        Integer ownerHoodsId = hoodsService.getUserHoods(threadsOwnerId);
        Integer userHoodsId = hoodsService.getUserHoods(userId);

        return ownerHoodsId != null && ownerHoodsId.equals(userHoodsId);
    }

    public List<String> searchThreadsByKeyWords(String keyWords) {
        return this.threadsMapper.searchThreadsByKeyWords("%" + keyWords + "%");
    }

    public List<Message> getCommentsByThreadsId(String threadsId) {
        return this.messageMapper.getCommentsByThreadsId(threadsId);
    }


}
