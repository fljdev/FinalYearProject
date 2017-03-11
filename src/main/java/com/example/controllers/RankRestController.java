package com.example.controllers;

import com.example.entities.Rank;
import com.example.entities.User;
import com.example.services.IRankService;
import com.example.services.IUserService;
import com.sun.tools.javac.util.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by admin on 02/03/2017.
 */


@RestController
@RequestMapping("/api/rank")
public class RankRestController {

    IRankService iRankService;
    IUserService iUserService;

    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }

    @Autowired
    public void setiRankService(IRankService service) {
        this.iRankService = service;
    }

    @RequestMapping(value = "/allRanks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<Rank> getAllRanks() {
        ArrayList<Rank> ranks = new ArrayList();
        ranks = (ArrayList<Rank>) iRankService.getALlRanks();
        return ranks;
    }

    @RequestMapping(value = "/saveRank", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<Rank> saveRank() {

        ArrayList<Rank> allTheRanks = iRankService.getALlRanks();

        for(Rank r : allTheRanks){
            System.out.println("got into the loop");
            iRankService.deleteRank(r);
            System.out.println("deleted one");
        }

        ArrayList<User> users = iUserService.getAllUsers();
        ArrayList<Double> balances = new ArrayList<>();
        for(User u : users){
            balances.add(u.getAccount().getBalance());
        }
        HashSet<Double> uniqueBalances = new HashSet<>(balances);

        int count = 1;
        for(Double d : uniqueBalances){
            for(User u : users){
                if(u.getAccount().getBalance()==d){
                    Rank rank = new Rank();
                    rank.setCurrentRank(count);
//                    u.setRank(rank);
                    rank.setUser(u);

                    iRankService.saveRank(rank);
//                    iUserService.register(u);
                }
            }
            count++;
        }

        ArrayList<Rank> rankedByBalanceList = iRankService.getALlRanks();
        System.out.println("rankedByBal: "+rankedByBalanceList.toString());
        return rankedByBalanceList;
    }





}
