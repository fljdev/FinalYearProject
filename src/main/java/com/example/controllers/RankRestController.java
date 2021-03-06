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
import java.util.List;

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
    public List<Rank> getAllRanks() {
        return iRankService.getALlRanks();
    }



    @RequestMapping(value = "/saveRank", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Rank> saveRank() {

        for(Rank r : iRankService.getALlRanks()){
            iRankService.deleteRank(r);
        }

        List<Double> balances = new ArrayList<>();

        for(User u : iUserService.getAllUsers()){
            balances.add(u.getAccount().getBalance());
        }
        List<Integer>balancesInteger = new ArrayList<>();
        for(Double d : balances){
            int x = (int)d.doubleValue();
            balancesInteger.add(x);
        }

        Collections.sort(balancesInteger);

        List<Double> newBalances = new ArrayList<>();
        for(Integer i : balancesInteger){
            double castBack = (double)i;
            newBalances.add(castBack);
        }
        HashSet<Double> uniqueBalances = new HashSet<>(newBalances);

        int count = uniqueBalances.size();
        for(Double d : uniqueBalances){
            for(User u : iUserService.getAllUsers()){
                int bal = (int)u.getAccount().getBalance();
                if(bal==d){
                    Rank rank = new Rank();
                    rank.setCurrentRank(count);
                    rank.setUser(u);

                    u.setCurrentRank(count);

                    if(u.getCurrentRank()<u.getBestRank() || u.getBestRank()== 0){
                        u.setBestRank(count);
                    }
                    iRankService.saveRank(rank);
                    iUserService.register(u);
                }
            }
            count--;
        }

        List<Rank> rankedByBalanceList = iRankService.getALlRanks();
        Collections.reverse(rankedByBalanceList);
        return rankedByBalanceList;
    }





}
