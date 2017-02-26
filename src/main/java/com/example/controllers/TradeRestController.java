package com.example.controllers;

import com.example.entities.Challenge;
import com.example.entities.User;
import com.example.services.ICurrencyPairService;
import com.example.services.ITradeService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 26/02/2017.
 */

@RestController
@RequestMapping("/api/trade")
public class TradeRestController {
    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IUserService iUserService;


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



    @RequestMapping(value = "/saveTrade", method = RequestMethod.POST, produces = "application/json")
    public void saveThisChallenge(@RequestBody String json){
        JSONObject jsonObject = new JSONObject(json);

        System.out.println(json);


        String playerID = jsonObject.getString("playerID");
        String pairID = jsonObject.getString("pairID");
        String stake = jsonObject.getString("stake");
        String action = jsonObject.getString("action");

        System.out.println("playerId : "+playerID);
        System.out.println("pair : "+pairID);
        System.out.println("stake: "+stake);
        System.out.println("action : "+action);


    }//end saveThidChallenge



}
