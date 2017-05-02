package com.example.controllers;


import com.example.entities.Result;
import com.example.entities.User;
import com.example.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    IStatsService iStatsService;
    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IUserService iUserService;
    IBankAccountService iBankAccountService;
    IResultService iResultService;

    @Autowired
    public void setiResultService(IResultService service){
        this.iResultService = service;
    }
    @Autowired
    public void setiStatsService(IStatsService iStatsService){
        this.iStatsService = iStatsService;
    }
    @Autowired
    public void setiBankAccountService(IBankAccountService iBankAccountService) {this.iBankAccountService = iBankAccountService;}
    @Autowired
    public void setiUserService(IUserService iUserService) {this.iUserService = iUserService;}
    @Autowired
    public void setiTradeService(ITradeService iTradeService){
        this.iTradeService = iTradeService;
    }
    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){this.iCurrencyPairService = iCurrencyPairService;}



    @RequestMapping(value = "/findByUser", method = RequestMethod.POST, produces = "application/json")
    public List<Result> findByUser(@RequestBody int id){
        User thisUser = iUserService.findById(id);
        return iResultService.findByUser(thisUser);
    }

    @RequestMapping(value = "/findWinsByUser", method = RequestMethod.POST, produces = "application/json")
    public List<Result> findWinsByUser(@RequestBody int id){
        User thisUser = iUserService.findById(id);

        List<Result>victories = new ArrayList<>();
        for(Result r : iResultService.getAllResults()){
            if(r.getWinner()==thisUser){
                victories.add(r);
            }
        }
        return victories;
    }

    @RequestMapping(value = "/findTotalWinnings", method = RequestMethod.POST, produces = "application/json")
    public double findTotalWinnings(@RequestBody int id){
        User thisUser = iUserService.findById(id);
        double total = 0;
        for(Result r : iResultService.getAllResults()){
            if(r.getWinner()==thisUser){
                total+=r.getPrize();
            }
        }
        return total;
    }

    @RequestMapping(value = "/findTotalLosses", method = RequestMethod.POST, produces = "application/json")
    public double findTotalLosses(@RequestBody int id){
        User thisUser = iUserService.findById(id);
        double total = 0;
        for(Result r : iResultService.getAllResults()){
            if(r.getLoser()==thisUser){
                total+=r.getPrize();
            }
        }
        return total;
    }

    @RequestMapping(value = "/findLossesByUser", method = RequestMethod.POST, produces = "application/json")
    public List<Result> findLossesByUser(@RequestBody int id){
        User thisUser = iUserService.findById(id);

        List<Result>losses = new ArrayList<>();
        for(Result r : iResultService.getAllResults()){
            if(r.getLoser()==thisUser){
                losses.add(r);
            }
        }
        return losses;
    }








}
