package com.nyu.nextdoor.controller;

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
import java.util.List;

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
    public Object getAll() {
        List<Blocks> blocksList = blocksServices.getAllBlocks();
        return new ResponseEntity<>(blocksList, HttpStatus.OK);
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


        //If there's no one in the blocks, no need to be approved
        if(blocksServices.getUserIdsInBlocks(blocksId).size() == 0) {
            blocksServices.addInBlocks(user.getUserId(), blocksId);
        } else {
            blocksServices.applyBlocks(user.getUserId(), blocksId);
        }

        return new ResponseEntity(HttpStatus.OK);


    }


    /*
     *   get applications in a specific blocks
     * */
    @CheckLogin
    @GetMapping("/application/{blocksId}")
    public Object getBlocksApplication(@PathVariable("blocksId") int blocksId,
                                       @RequestHeader(value = "token") String token) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);
        if (!blocksServices.checkUserInBlocks(user.getUserId(), blocksId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            List<BlocksApplication> blocksApplications = blocksServices.getBlocksApplications(blocksId);
            return new ResponseEntity<>(blocksApplications, HttpStatus.OK);
        }
    }

    /*
     *   Approve blocks application
     * */
    @CheckLogin
    @PostMapping("/application/approval")
    public Object aproveBlocksApplication(@RequestHeader(value = "token") String token,
                                          @RequestBody BlocksApplication blocksApplication) throws AccessDeniedException {
        User user = authenticationService.getUserFromToken(token);

        if (!blocksServices.checkUserInBlocks(user.getUserId(), blocksApplication.getBlocksId())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int numberPeople = blocksServices.getUserIdsInBlocks(blocksApplication.getBlocksId()).size();
        blocksApplication = blocksServices.getBlocksApplication(blocksApplication.getApplicationId());

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


}
