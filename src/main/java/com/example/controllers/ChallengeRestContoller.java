package com.example.controllers;

import com.example.services.IChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 18/02/2017.
 */

@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestContoller {

    IChallengeService iChallengeService;

    @Autowired
    public void setiChallengeService(IChallengeService service){
        this.iChallengeService = service;
    }


}
