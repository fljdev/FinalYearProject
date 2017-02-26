package com.example.controllers;

import com.example.entities.Challenge;
import com.example.entities.CurrencyPair;
import com.example.entities.Trade;
import com.example.entities.User;
import com.example.forex.ForexDriver;
import com.example.services.ICurrencyPairService;
import com.example.services.ITradeService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;

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
    public void saveThisChallenge(@RequestBody String json)throws Exception{
        JSONObject jsonObject = new JSONObject(json);

        System.out.println(json);


        String playerID = jsonObject.getString("playerID");
        String pairSymbols = jsonObject.getString("pairSymbols");
        String stakeString = jsonObject.getString("stake");
        double stake = Double.parseDouble(stakeString);
        String action = jsonObject.getString("action");

        System.out.println("playerId : "+playerID);
        System.out.println("pair : "+pairSymbols);
        System.out.println("stake: "+stake);
        System.out.println("action : "+action);

        Trade trade = new Trade();

        Timestamp timestampOpen = new Timestamp(System.currentTimeMillis());


        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair = thisPair(pairSymbols);
        iCurrencyPairService.saveCurrencyPair(currencyPair);

        User user = new User();
        user = findById(playerID);

        trade.setUser(user);
        trade.setCurrencyPair(currencyPair);
        trade.setStake(stake);
        trade.setAction(action);
        trade.setTimestampOpen(timestampOpen);

        iTradeService.saveTrade(trade);


    }//end saveTrade

    @RequestMapping(value ="/getThisPair", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CurrencyPair thisPair(@RequestBody String symbols)throws Exception{
        CurrencyPair thisPair = new CurrencyPair();
        ArrayList<CurrencyPair> pairs = allPairs();

        for(CurrencyPair cp : pairs){
            if(cp.getSymbols().equalsIgnoreCase(symbols)){
                thisPair = cp;
            }
        }
        return thisPair;
    }



    @RequestMapping(value ="/pairs", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<CurrencyPair> allPairs()throws Exception{
        ForexDriver tester = new ForexDriver();

        tester.currencyPairs.clear();

        tester.makeCurrencyPairs(tester.rawResponseList);
        tester.rawResponseList.clear();

        ArrayList<CurrencyPair>pairs= new ArrayList<CurrencyPair>();
        pairs = tester.getCurrencyPairs();


        System.out.println("pairs came in as "+pairs.toString());
        return pairs;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public User findById(@RequestBody String id){

        int i = Integer.parseInt(id);
        ArrayList<User> users = iUserService.getAllUsers();

        for(User u : users){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }



}
