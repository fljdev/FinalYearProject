package com.example.controllers;

import com.example.entities.BankAccount;
import com.example.entities.Rank;
import com.example.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 26/04/2017.
 */
@RestController
@RequestMapping("/api/home")
public class HomeRestController {

    IChallengeService iChallengeService;
    IUserService iUserService;
    IGameAccountService iGameAccountService;
    IResultService iResultService;
    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IBankAccountService iBankAccountService;
    @Autowired
    public void setiBankAccountService(IBankAccountService service){
        this.iBankAccountService=service;
    }
    @Autowired
    public void setiTradeService(ITradeService iTradeService){
        this.iTradeService = iTradeService;
    }
    @Autowired
    public void setiResultService(IResultService service){
        this.iResultService = service;
    }
    @Autowired
    public void setiGameAccountService(IGameAccountService iGameAccountService){
        this.iGameAccountService = iGameAccountService;
    }
    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }
    @Autowired
    public void setiChallengeService(IChallengeService service){

        this.iChallengeService = service;
    }
    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }

    @RequestMapping(value = "/averageBalance", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Double> getAverageAccountBalance() {
        List<Double>accountBalances=new ArrayList<>();
        for(BankAccount b : iBankAccountService.findAllAccounts()){
            System.out.println("adding this balance: ");
            accountBalances.add(b.getBalance());
        }
        return accountBalances;
    }


}
