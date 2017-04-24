package com.example.controllers;

import com.example.entities.*;
import com.example.forex.ForexDriver;
import com.example.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    @RequestMapping(value = "/saveGameTrade", method = RequestMethod.POST)
    public Trade saveThisTrade(@RequestBody String json)throws Exception{
        JSONObject jsonObject = new JSONObject(json);
        Timestamp timestampOpen = new Timestamp(System.currentTimeMillis());

        String pairSymbols = jsonObject.getString("pairSymbols");
        double margin = jsonObject.getDouble("margin");
        String action = jsonObject.getString("action");
        double positionUnits = jsonObject.getDouble("positionUnits");

        Trade trade = new Trade();

        CurrencyPair currencyPair = thisPair(pairSymbols);
        currencyPair.setActive(true);
        iCurrencyPairService.saveCurrencyPair(currencyPair);

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("playerID")));

        Challenge chall = iChallengeService.findById(jsonObject.getInt("challengeID"));

        GameAccount gameAccount = iGameAccountService.findGameAccountByUserAndChallenge(user,chall);
        double currentBalance = gameAccount.getBalance();
        double updatedBalance = currentBalance - margin;
        gameAccount.setBalance(updatedBalance);
        gameAccount.setUser(user);
        gameAccount.setChallenge(chall);
        iGameAccountService.register(gameAccount);

        double existingGameMargin = user.getGameMargin();
        double updatedGameMargin = existingGameMargin + margin;
        user.setGameMargin(updatedGameMargin);

        iUserService.register(user);

        trade.setCurrencyPairOpen(currencyPair);
        trade.setMargin(margin);
        trade.setAction(action);
        trade.setTimestampOpen(timestampOpen);
        trade.setPositionUnits(positionUnits);
        trade.setUser(user);
        trade.setChallenge(chall);
        iTradeService.saveTrade(trade);

        return trade;
    }//end saveTrade

    @RequestMapping(value = "/closeGameTrade", method = RequestMethod.POST)
    public Trade closeThisTrade(@RequestBody String closeParams)throws Exception {

        JSONObject jsonObject = new JSONObject(closeParams);

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("userId")));
        Trade tradeToClose = iTradeService.findTradeById(Integer.parseInt(jsonObject.getString("tradeId")));

        Challenge challenge = iChallengeService.findById(Integer.parseInt(jsonObject.getString("challengeId")));

        if(tradeToClose!=null){
            tradeToClose.setOpen(false);

            GameAccount gameAccount = iGameAccountService.findGameAccountByUserAndChallenge(user,challenge);

            double currentBalance = gameAccount.getBalance();
            double balanceUpdate = currentBalance + tradeToClose.getMargin() + tradeToClose.getClosingProfitLoss();
            gameAccount.setBalance(balanceUpdate);
            iGameAccountService.register(gameAccount);

            Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
            tradeToClose.setTimestampClose(timestampClose);

            CurrencyPair closingPair = thisPair(tradeToClose.getCurrencyPairOpen().getSymbols());
            tradeToClose.setCurrencyPairClose(closingPair);

            iCurrencyPairService.saveCurrencyPair(closingPair);
            iTradeService.updateAndSaveTrade(tradeToClose);

            double currGameProfit = user.getGameProfit();
            double thisTradePL = tradeToClose.getClosingProfitLoss();
            currGameProfit -= thisTradePL;
            user.setGameProfit(currGameProfit);

            double existingGameMargin = user.getGameMargin();
            double updatedGameMargin = existingGameMargin - tradeToClose.getMargin();
            user.setGameMargin(updatedGameMargin);

            iUserService.register(user);
        }
        return tradeToClose;
    }



    @RequestMapping(value ="/updateEachGameTrade", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> live(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));
        List<Trade> openTradesWithChallenges = new ArrayList<>();
        if(user!=null){

            List<Trade> openTrades = iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList());
            for(Trade t : openTrades){
                if(t.getChallenge()!=null){
                    openTradesWithChallenges.add(t);
                }
            }

            LiveTradeInfo liveTradeInfo;

            for(Trade t : openTradesWithChallenges){

                liveTradeInfo = new LiveTradeInfo();

                CurrencyPair thisCurrencyPair = thisPair(t.getCurrencyPairOpen().getSymbols());

                Timestamp tickTime = new Timestamp(System.currentTimeMillis());

                liveTradeInfo.setTradeID(t.getId());

                liveTradeInfo.setTickTime(tickTime);

                liveTradeInfo.setCurrentAsk(thisCurrencyPair.getAsk());

                liveTradeInfo.setCurrentBid(thisCurrencyPair.getBid());

                double profit = calcThisProfitAndLoss(t,thisCurrencyPair);
                System.out.println("in updateEachGemeTrade and profit is "+profit);
                System.out.println("profit is "+profit);
                t.setClosingProfitLoss(profit);

                liveTradeInfo.setCurrentProfitAndLoss(profit);

                iLiveTradeInfoService.saveLiveTradeInfo(liveTradeInfo);

                t.getLiveTradeInfoList().add(liveTradeInfo);


                iTradeService.saveTrade(t);
            }
        }
        return openTradesWithChallenges;
    }


    @RequestMapping(value ="/getTotalProfitAndLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User totalProfit(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));

        double gameProfit=0;
        if(user!=null){
            for(Trade t : iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList())){

                if(t.getChallenge()!=null){
                    gameProfit += t.getClosingProfitLoss();
                    user.setGameProfit(gameProfit);

                    iUserService.register(user);
                }
            }
        }
        return user;
    }




    @RequestMapping(value = "/findUserChallengeGameAccount", method = RequestMethod.POST)
    public GameAccount findUserChallengeGameAccount(@RequestBody String json)throws Exception {

        JSONObject jsonObject = new JSONObject(json);
        User u = iUserService.findById(Integer.parseInt(jsonObject.getString("userId")));
        Challenge c = iChallengeService.findById(Integer.parseInt(jsonObject.getString("challId")));
        GameAccount ga = iGameAccountService.findGameAccountByUserAndChallenge(u,c);
        return ga;
    }

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


    private double calcThisProfitAndLoss(Trade thisTrade, CurrencyPair thisPairOpen)throws Exception{
        String symbols = thisTrade.getCurrencyPairOpen().getSymbols();
        double profit=0;
        double positionUnits = thisTrade.getPositionUnits();
        double longPipDiff = thisTrade.getCurrencyPairOpen().getBid() - thisPairOpen.getAsk();
        double shortPipDiff = thisPairOpen.getBid() - thisTrade.getCurrencyPairOpen().getAsk();


        if(symbols.contains("/USD")){

            profit = calculateDirectQuote(thisTrade,thisPairOpen,longPipDiff,shortPipDiff,positionUnits);
        }  else if(thisTrade.getCurrencyPairOpen().getSymbols().contains("USD/")){

            profit = calculateIndirectQuote(thisTrade,thisPairOpen,longPipDiff,shortPipDiff,positionUnits);
        }
        return profit;
    }

    private double calculateDirectQuote(Trade thisTrade , CurrencyPair thisPairOpen,double longPipDiff,double shortPipDiff,double positionUnits){
        return (thisTrade.getAction().equalsIgnoreCase("buy")) ? longPipDiff * positionUnits : shortPipDiff * positionUnits;
    }


    private double calculateIndirectQuote(Trade thisTrade , CurrencyPair thisPairOpen,double longPipDiff,double shortPipDiff,double positionUnits){
        return (thisTrade.getAction().equalsIgnoreCase("buy"))?(longPipDiff * positionUnits) /thisTrade.getCurrencyPairOpen().getBid() : (shortPipDiff * positionUnits) /thisTrade.getCurrencyPairOpen().getAsk();
    }

    @RequestMapping(value = "/getOpenGameTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> findOpenGameTrades(@RequestBody String userId){
        User currentUser = iUserService.findById(Integer.parseInt(userId));

        List<Trade>openTrades = iTradeService.findByUser(currentUser).stream().filter(Trade::isOpen).collect(Collectors.toList());

        List<Trade> openTradesWithChallenges = new ArrayList<>();
        for(Trade t : openTrades){
            if(t.getChallenge()!=null){
                openTradesWithChallenges.add(t);
            }
        }
        return openTradesWithChallenges;
    }
}
