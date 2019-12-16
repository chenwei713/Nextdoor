package com.nyu.nextdoor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.nyu.nextdoor.annotation.CheckLogin;
import com.nyu.nextdoor.model.Blocks;
import com.nyu.nextdoor.model.BlocksApplication;
import com.nyu.nextdoor.model.User;
import com.nyu.nextdoor.service.AuthenticationService;
import com.nyu.nextdoor.service.BlocksServices;
import com.nyu.nextdoor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/blocks")
public class BlocksController {
    private UserService userService;
    private BlocksServices blocksServices;
    private AuthenticationService authenticationService;

    @Autowired
    BlocksController(BlocksServices blocksServices, UserService userService, AuthenticationService authenticationService) {
        this.blocksServices = blocksServices;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /*
     *   get all blocks
     * */
    @CheckLogin
    @GetMapping("/all")
    public Object getAll(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        List<Blocks> blocksList = blocksServices.getAllBlocks();
        User user = authenticationService.getUserFromToken(token);

        List<Blocks> availableBlocksList = new ArrayList<>();
        for(Blocks blocks: blocksList) {
            if((blocks.getLatitude1() <= user.getLatitude() && user.getLatitude() <= blocks.getLatitude2()
                    || blocks.getLatitude2() <= user.getLatitude() && user.getLatitude() <= blocks.getLatitude1())
                    &&
                    (blocks.getLongitude1() <= user.getLongitude() && user.getLongitude() <= blocks.getLongitude2()
                            || blocks.getLongitude2() <= user.getLongitude() && user.getLongitude() <= blocks.getLongitude1())) {
                availableBlocksList.add(blocks);
            }
        }

        return new ResponseEntity<>(availableBlocksList, HttpStatus.OK);
    }


    /*
     *   send an application for blocks
     * */
    @CheckLogin
    @PostMapping("/application/{blocksId}")
    public Object applyBlocks(@PathVariable("blocksId") int blocksId,
                              @RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);

        if (user == null) {
            // TODO: Give more details about error messages
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // Check if candidate already in other blocks
        Integer checkBlocksId = blocksServices.getUserBlocks(user.getUserId());
        if(checkBlocksId != null) {
            Map<String, String> response = new HashMap<>();
            // This attribute can be deleted in db later, useless
            response.put("Msg", "This user already in other blocks");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }


        //If there's no one in the blocks, no need to be approved
        if(blocksServices.getUserIdsInBlocks(blocksId).size() == 0) {
            blocksServices.addInBlocks(user.getUserId(), blocksId);
        } else {
            blocksServices.applyBlocks(user.getUserId(), blocksId);
        }

        return new ResponseEntity(HttpStatus.OK);


    }

    /*
     *   get applications in blocks of user
     * */
    @CheckLogin
    @GetMapping("/application")
    public Object getBlocksApplication(@RequestHeader(value = "token") String token) throws AccessDeniedException, JsonProcessingException {
        User user = authenticationService.getUserFromToken(token);
        Integer blocksId = blocksServices.getUserBlocks(user.getUserId());

        List<BlocksApplication> blocksApplications;
        if(blocksId == null) {
            blocksApplications = new ArrayList<>();
            return new ResponseEntity<>(blocksApplications, HttpStatus.OK);
        }


        blocksApplications = blocksServices.getBlocksApplications(blocksId);

        Gson gson = new Gson();
        List<HashMap> responses = new ArrayList<>();
        for(BlocksApplication blocksApplication: blocksApplications) {
            String jsonString = gson.toJson(blocksApplication);
            HashMap response = gson.fromJson(jsonString, HashMap.class);
            response.put("name", userService.getUserByID(blocksApplication.getUserId()).getUserFirstName());
            responses.add(response);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /*
     *   Approve blocks application
     * */
    @CheckLogin
    @PostMapping("/application/approval/{blocksApplicationId}")
    public Object aproveBlocksApplication(@RequestHeader(value = "token") String token,
                                          @PathVariable("blocksApplicationId") int blocksApplicationId) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        BlocksApplication blocksApplication = blocksServices.getBlocksApplication(blocksApplicationId);

        if (!blocksServices.checkUserInBlocks(user.getUserId(), blocksApplication.getBlocksId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int numberPeople = blocksServices.getUserIdsInBlocks(blocksApplication.getBlocksId()).size();
        blocksApplication = blocksServices.getBlocksApplication(blocksApplication.getApplicationId());

        // Check if candidate already in other blocks
        Integer blocksId = blocksServices.getUserBlocks(blocksApplication.getUserId());
        if(blocksId != null) {
            Map<String, String> response = new HashMap<>();
            // This attribute can be deleted in db later, useless
            blocksApplication.setIsValid(0);
            blocksServices.updateBlocksApplication(blocksApplication);
            response.put("Msg", "This user already in other blocks");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        if(blocksApplication.getApproval1() == null) {
            blocksApplication.setApproval1(user.getUserId());
            if(numberPeople == 1) {
                blocksApplication.setIsApproved(1);
                blocksServices.addInBlocks(blocksApplication.getUserId(), blocksApplication.getBlocksId());
            }
        } else if(blocksApplication.getApproval2() == null && !blocksApplication.getApproval1().equals(user.getUserId())) {
            blocksApplication.setApproval2(user.getUserId());
            if(numberPeople == 2) {
                blocksApplication.setIsApproved(1);
                blocksServices.addInBlocks(blocksApplication.getUserId(), blocksApplication.getBlocksId());
            }
        } else if(blocksApplication.getApproval3() == null && !blocksApplication.getApproval1().equals(user.getUserId())
                && !blocksApplication.getApproval2().equals(user.getUserId())) {
            blocksApplication.setApproval3(user.getUserId());
            blocksApplication.setIsApproved(1);
            blocksServices.addInBlocks(blocksApplication.getUserId(), blocksApplication.getBlocksId());
        }
        blocksServices.updateBlocksApplication(blocksApplication);

        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    *   Get all people in block
    * */
    @CheckLogin
    @GetMapping("")
    public Object getPeopleList(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);

        if(user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Integer blocksId = blocksServices.getUserBlocks(user.getUserId());

        if(blocksId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<Integer> userIdsInBlocks = blocksServices.getUserIdsInBlocks(blocksId);

        return new ResponseEntity<>(userIdsInBlocks, HttpStatus.OK);

    }

    /*
    *   Get user block id
    * */
    @CheckLogin
    @GetMapping("/id")
    public Object get(@RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        Integer blocksId = blocksServices.getUserBlocks(user.getUserId());
        Map<String, Integer> response = new HashMap<>();
        response.put("blocksId", blocksId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
