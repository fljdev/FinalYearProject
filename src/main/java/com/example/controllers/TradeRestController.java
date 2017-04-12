package com.example.controllers;

import com.example.entities.*;
import com.example.forex.ForexDriver;
import com.example.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/trade")
public class TradeRestController {

    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IUserService iUserService;
    IBankAccountService iBankAccountService;
    ILiveTradeInfo iLiveTradeInfo;
    @Autowired
    public void setiLiveTradeInfo(ILiveTradeInfo info){
        this.iLiveTradeInfo=info;
    }
    @Autowired
    public void setiBankAccountService(IBankAccountService iBankAccountService) {

        this.iBankAccountService = iBankAccountService;
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







    @RequestMapping(value = "/saveTrade", method = RequestMethod.POST)
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

        BankAccount account = user.getAccount();
        double currentBalance = account.getBalance();
        double updatedBalance = currentBalance - margin;
        account.setBalance(updatedBalance);
        iBankAccountService.register(account);

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

        iTradeService.saveTrade(trade);

        return trade;
    }//end saveTrade



    @RequestMapping(value ="/getTotalProfitAndLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User totalProfit(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));
        List<Trade> openTrades = new ArrayList<>();

        double totalProfit=0;

        if(user!=null){
            openTrades = iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList());
            for(Trade t : openTrades){

                totalProfit += t.getClosingProfitLoss();
                user.setCurrentProfit(totalProfit);

                iUserService.register(user);
            }

        }
        return user;
    }



    @RequestMapping(value ="/updateEachTrade", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> live(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));
        List<Trade> openTrades = new ArrayList<>();
        if(user!=null){

            //Finds trades by user obj, filters via open trades, adds to collection (LIST)
            openTrades = iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList());


            LiveTradeInfo liveTradeInfo;

            for(Trade t : openTrades){

                liveTradeInfo = new LiveTradeInfo();

                CurrencyPair thisCurrencyPair = thisPair(t.getCurrencyPairOpen().getSymbols());

                Timestamp tickTime = new Timestamp(System.currentTimeMillis());

                liveTradeInfo.setTradeID(t.getId());

                liveTradeInfo.setTickTime(tickTime);

                liveTradeInfo.setCurrentAsk(thisCurrencyPair.getAsk());

                liveTradeInfo.setCurrentBid(thisCurrencyPair.getBid());

                double profit = calcThisProfitAndLoss(t,thisCurrencyPair);
                System.out.println("profit is "+profit);
                t.setClosingProfitLoss(profit);

                liveTradeInfo.setCurrentProfitAndLoss(profit);

                iLiveTradeInfo.saveLiveTradeInfo(liveTradeInfo);

                t.getLiveTradeInfoList().add(liveTradeInfo);


                iTradeService.saveTrade(t);
            }
        }
        return openTrades;
    }



    @RequestMapping(value = "/closeLiveTrade", method = RequestMethod.POST)
    public Trade closeThisTrade(@RequestBody String closeParams)throws Exception {

        JSONObject jsonObject = new JSONObject(closeParams);
        String symbols = jsonObject.getString("sym");

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("id")));
        Trade tradeToClose = iTradeService.findBySymbols(symbols,user);

        if(tradeToClose!=null){
            tradeToClose.setOpen(false);

            Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
            tradeToClose.setTimestampClose(timestampClose);
            CurrencyPair closingPair = thisPair(symbols);
            tradeToClose.setCurrencyPairClose(closingPair);
            iCurrencyPairService.saveCurrencyPair(closingPair);
            iTradeService.updateAndSaveTrade(tradeToClose);

            double currBal = user.getCurrentProfit();
            double thisTradePL = tradeToClose.getClosingProfitLoss();
            currBal -= thisTradePL;
            user.setCurrentProfit(currBal);

            double existingMargin = user.getTotalMargin();
            double updatedMargin = existingMargin - tradeToClose.getMargin();
            user.setTotalMargin(updatedMargin);
            iUserService.register(user);
        }
        return tradeToClose;
    }








    private double calcThisProfitAndLoss(Trade thisTrade, CurrencyPair thisPairOpen)throws Exception{
        double profit=0;
        if(thisTrade.getCurrencyPairOpen().getSymbols().contains("/USD")){
            profit = calculateDirectQuote(thisTrade,thisPairOpen);
        }  else if(thisTrade.getCurrencyPairOpen().getSymbols().contains("USD/")){
            profit = calculateIndirectQuote(thisTrade,thisPairOpen);
        }
        return profit;
    }

    private double calculateDirectQuote(Trade thisTrade , CurrencyPair thisPairOpen){

        double positionUnits = thisTrade.getPositionUnits();
        double longPipDiff = thisTrade.getCurrencyPairOpen().getBid() - thisPairOpen.getAsk();
        double shortPipDiff = thisPairOpen.getBid() - thisTrade.getCurrencyPairOpen().getAsk();
        if(thisTrade.getAction().equalsIgnoreCase("buy")){
            return longPipDiff * positionUnits;
        }
        return shortPipDiff * positionUnits;
    }


    private double calculateIndirectQuote(Trade thisTrade , CurrencyPair thisPairOpen){

        double positionUnits = thisTrade.getPositionUnits();
        double longPipDiff = thisTrade.getCurrencyPairOpen().getBid() - thisPairOpen.getAsk();
        double shortPipDiff = thisPairOpen.getBid() - thisTrade.getCurrencyPairOpen().getAsk();
        if(thisTrade.getAction().equalsIgnoreCase("buy")){
            return (longPipDiff * positionUnits) /thisTrade.getCurrencyPairOpen().getBid();
        }
        return (shortPipDiff * positionUnits) /thisTrade.getCurrencyPairOpen().getAsk();
    }


//        List<Trade> openTrades = findOpenTrades(playerID);
//
//        for(Trade t : openTrades){
//            if(t.getCurrencyPairOpen().getSymbols().equalsIgnoreCase(pairSymbols)){
//
//                System.out.println("This trade is "+t.getId());
//
//                Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
//
//                t.setTimestampClose(timestampClose);
//                t.setCurrencyPairClose(closingPair);
//
//
//
//
////                t.setClosingProfitLoss(profit);
//                iTradeService.saveTrade(t);
//
////                User user = findById(playerID);
////                BankAccount bankAccount = user.getAccount();
////                double marginPayed = t.getMargin();
////                double currentBalance = bankAccount.getBalance();
////                double updatedBalance = currentBalance+marginPayed;
////                updatedBalance +=profit;
////                bankAccount.setBalance(updatedBalance);
////                iBankAccountService.register(bankAccount);
////
////                iUserService.register(user);
//            }








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
        return pairs;
    }



    @RequestMapping(value = "/getAllTrades", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Trade> findAllTrades(){
        return iTradeService.getAllTrades();
    }

    @RequestMapping(value = "/getOpenTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> findOpenTrades(@RequestBody String userId){
        User currentUser = iUserService.findById(Integer.parseInt(userId));
        return iTradeService.findByUser(currentUser).stream().filter(Trade::isOpen).collect(Collectors.toList());
    }



}
