package com.example.controllers;

import com.example.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 22/04/2017.
 */
@RestController
@RequestMapping("/api/gameTrade")
public class GameTradeRestController {
    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IUserService iUserService;
    ILiveTradeInfoService iLiveTradeInfoService;
    IGameAccountService iGameAccountService;

    @Autowired
    public void setiLiveTradeInfoService(ILiveTradeInfoService info){
        this.iLiveTradeInfoService =info;
    }
    @Autowired
    public void setGameAccountService(IGameAccountService iGameAccountService) {

        this.iGameAccountService = iGameAccountService;
    }
    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }
    @Autowired
    public void setiTradeService(ITradeService iTradeService){
        this.iTradeService = iTradeService;
    }
    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }



}
