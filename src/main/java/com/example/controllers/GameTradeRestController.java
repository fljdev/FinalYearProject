package com.example.controllers;

import com.example.entities.*;
import com.example.forex.ForexDriver;
import com.example.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;

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
    IChallengeService iChallengeService;

    @Autowired
    public void setiChallengeService(IChallengeService  service){
        this.iChallengeService=service;
    }

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


    @RequestMapping(value = "/closeGameTrade", method = RequestMethod.POST)
    public Trade closeThisTrade(@RequestBody String closeParams)throws Exception {

        JSONObject jsonObject = new JSONObject(closeParams);
        String symbols = jsonObject.getString("sym");

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("id")));
        Trade tradeToClose = iTradeService.findBySymbols(symbols,user);

        if(tradeToClose!=null){
            tradeToClose.setOpen(false);

            GameAccount gameAccount = user.getGameAccount();
            double currentBalance = gameAccount.getBalance();
            double balanceUpdate = currentBalance + tradeToClose.getMargin() + tradeToClose.getClosingProfitLoss();
            gameAccount.setBalance(balanceUpdate);
            iGameAccountService.register(gameAccount);


            Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
            tradeToClose.setTimestampClose(timestampClose);
            CurrencyPair closingPair = thisPair(symbols);
            tradeToClose.setCurrencyPairClose(closingPair);
            iCurrencyPairService.saveCurrencyPair(closingPair);
            iTradeService.updateAndSaveTrade(tradeToClose);

//            double currBal = user.getCurrentProfit();
//            double thisTradePL = tradeToClose.getClosingProfitLoss();
//            currBal -= thisTradePL;
//            user.setCurrentProfit(currBal);

//            double existingMargin = user.getTotalMargin();
//            double updatedMargin = existingMargin - tradeToClose.getMargin();
//            user.setTotalMargin(updatedMargin);
            iUserService.register(user);
        }
        return tradeToClose;
    }

    @RequestMapping(value = "/saveGameTrade", method = RequestMethod.POST)
    public Trade saveThisTrade(@RequestBody String json)throws Exception{
        JSONObject jsonObject = new JSONObject(json);
        Timestamp timestampOpen = new Timestamp(System.currentTimeMillis());

        System.out.println("select * frava");

        String pairSymbols = jsonObject.getString("pairSymbols");
        double margin = jsonObject.getDouble("margin");
        String action = jsonObject.getString("action");
        double positionUnits = jsonObject.getDouble("positionUnits");

        Challenge chall = iChallengeService.findById(jsonObject.getInt("challengeID"));

        Trade trade = new Trade();


        CurrencyPair currencyPair = thisPair(pairSymbols);
        currencyPair.setActive(true);
        iCurrencyPairService.saveCurrencyPair(currencyPair);

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("playerID")));

        GameAccount account = user.getGameAccount();
        double currentBalance = account.getBalance();
        double updatedBalance = currentBalance - margin;
        account.setBalance(updatedBalance);
        iGameAccountService.register(account);

        double existingMargin = user.getTotalMargin();
        double updatedMargin = existingMargin + margin;
        user.setTotalMargin(updatedMargin);

        iUserService.register(user);

        trade.setUser(user);
        trade.setCurrencyPairOpen(currencyPair);
        trade.setMargin(margin);
        trade.setAction(action);
        trade.setTimestampOpen(timestampOpen);
        trade.setPositionUnits(positionUnits);

        trade.setChallenge(chall);

        iTradeService.saveTrade(trade);

        return trade;
    }//end saveTrade


    @RequestMapping(value ="/getThisPair", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CurrencyPair thisPair(@RequestBody String symbols)throws Exception{
        CurrencyPair thisPair = new CurrencyPair();
        ArrayList<CurrencyPair> pairs = allCurrencyPairs();

        for(CurrencyPair cp : pairs){
            if(cp.getSymbols().equalsIgnoreCase(symbols)){
                thisPair = cp;
            }
        }
        return thisPair;
    }

    @RequestMapping(value ="/pairs", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<CurrencyPair> allCurrencyPairs()throws Exception{

        ForexDriver test = new ForexDriver();

        test.currencyPairs.clear();
        test.makeCurrencyPairs(test.rawResponseList);
        test.rawResponseList.clear();
        ArrayList<CurrencyPair>pairs= new ArrayList<CurrencyPair>();
        pairs = test.getCurrencyPairs();
        return pairs;
    }
}
