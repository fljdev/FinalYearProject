package com.example.controllers;

import com.example.entities.Challenge;
import com.example.entities.User;
import com.example.services.IChallengeService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by admin on 18/02/2017.
 */

@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestContoller {

    IChallengeService iChallengeService;
    IUserService iUserService;

    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }

    @Autowired
    public void setiChallengeService(IChallengeService service){
        this.iChallengeService = service;
    }

    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST, produces = "application/json")
    public void saveThisChallenge(@RequestBody String id){
        JSONObject jsonObject = new JSONObject(id);
        int currID = jsonObject.getInt("0");
        int oppID = jsonObject.getInt("1");

        Challenge thisChallenge = new Challenge();
        thisChallenge.setChallengerId(currID);
        thisChallenge.setOpponentId(oppID);
        iChallengeService.saveChallenge(thisChallenge);
    }//end saveThidChallenge
}
