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
    public ArrayList<Rank> getAllUsers() {
        ArrayList<Rank> ranks = new ArrayList();
        ranks = (ArrayList<Rank>) iRankService.getALlRanks();
        return ranks;
    }

    @RequestMapping(value = "/saveRank", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<User> saveRank() {

        ArrayList<User> users = iUserService.getAllUsers();
        ArrayList<User> rankedByBalanceList = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();

        for(User u : users){
            balances.add(u.getAccount().getBalance());
        }
        Collections.sort(balances);

        for(int i = balances.size()-1; i>=0 ; i--){
            for(User u : users){
                if(u.getAccount().getBalance()==balances.get(i)){
                    rankedByBalanceList.add(u);
                }
            }
        }

        for(User u : rankedByBalanceList){
            System.out.println("user in ranked list is "+u.getUsername()+" £"+u.getAccount().getBalance());
        }


        Rank rank = new Rank();





        return rankedByBalanceList;
    }





}
