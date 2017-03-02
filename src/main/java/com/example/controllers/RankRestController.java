package com.example.controllers;

import com.example.entities.Rank;
import com.example.services.IRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by admin on 02/03/2017.
 */


@RestController
@RequestMapping("/api/rank")
public class RankRestController {

    IRankService iRankService;

    @Autowired
    public void setiRankService(IRankService service) {
        this.iRankService = service;
    }

    @RequestMapping(value = "/allRanks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<Rank> getAllUsers() {
        ArrayList<Rank> ranks = new ArrayList();
        ranks = (ArrayList<Rank>) iRankService.getALlRanks();
        return ranks;
    }




}
