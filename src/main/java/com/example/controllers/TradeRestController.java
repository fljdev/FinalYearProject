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

        String playerID = jsonObject.getString("playerID");
        String pairSymbols = jsonObject.getString("pairSymbols");
        String marginString = jsonObject.getString("margin");
        double margin = Double.parseDouble(marginString);
        String action = jsonObject.getString("action");
        double positionUnits = jsonObject.getDouble("positionUnits");

        Trade trade = new Trade();

        Timestamp timestampOpen = new Timestamp(System.currentTimeMillis());

        CurrencyPair currencyPair = thisPair(pairSymbols);
        currencyPair.setActive(true);
        iCurrencyPairService.saveCurrencyPair(currencyPair);

        User user = findById(playerID);

        BankAccount account = user.getAccount();
        double currentBalance = account.getBalance();
        double updatedBalance = currentBalance - margin;
        account.setBalance(updatedBalance);
        iBankAccountService.register(account);

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

                calcThisProfitAndLoss(t,thisCurrencyPair);

                iLiveTradeInfo.saveLiveTradeInfo(liveTradeInfo);

                t.getLiveTradeInfoList().add(liveTradeInfo);

                iTradeService.saveTrade(t);
            }
        }
        return openTrades;
    }




//        List<LiveTradeInfo> liveTradeInfoList = thisTrade.getLiveTradeInfoList();
//        thisTrade.setLiveTradeInfoList(liveTradeInfoList);
//
//
//
//
//                System.out.println("xxxxxx"+thisTradePairSymbols);
//
//                LiveTradeInfo liveTradeInfo = new LiveTradeInfo();
//
//                CurrencyPair thisCurrencyPair = thisPair(thisTradePairSymbols);
//
//                Timestamp tickTime = new Timestamp(System.currentTimeMillis());
//
//                liveTradeInfo.setTradeID(tradeID);
//
//                liveTradeInfo.setTickTime(tickTime);
//
//                liveTradeInfo.setCurrentAsk(thisCurrencyPair.getAsk());
//
//                liveTradeInfo.setCurrentBid(thisCurrencyPair.getBid());
//
//
//                liveTradeInfoList.add(liveTradeInfo);
//
//                iLiveTradeInfo.saveLiveTradeInfo(liveTradeInfo);
//
//                iTradeService.saveTrade(thisTrade);
//
//
//                Thread.sleep(2000);






    private double calcThisProfitAndLoss(Trade thisTrade, CurrencyPair thisPairOpen)throws Exception{

        /**
         * Opening position size
         */
        double openAsk = thisPairOpen.getAsk();
        double openBid = thisPairOpen.getBid();

        double positionUnits = thisTrade.getPositionUnits();
        String action = thisTrade.getAction();
        double openPositionSize;
        if(action.equalsIgnoreCase("buy")){
            openPositionSize = positionUnits * openAsk;
            System.out.println("open position size is "+openPositionSize);


        }else{
            openPositionSize = positionUnits * openBid;
            System.out.println("open position size is "+openPositionSize);
        }

        CurrencyPair thisPairCurrent = thisPair(thisPairOpen.getSymbols());

        return 0;
    }

    @RequestMapping(value = "/closeLiveTrade", method = RequestMethod.POST)
    public void closeThisTrade(@RequestBody String closeParams)throws Exception {

        System.out.println("got here "+ closeParams);

        JSONObject jsonObject = new JSONObject(closeParams);
        String symbols = jsonObject.getString("sym");
        String userID = jsonObject.getString("id");
        int userId = Integer.parseInt(userID);

        User user = iUserService.findById(userId);
        Trade tradeToClose = null;

        for(Trade t : iTradeService.getAllTrades()){
            if(t.getUser()==user && t.getCurrencyPairOpen().getSymbols().equalsIgnoreCase(symbols)){
                tradeToClose = t;
            }
        }

        if(tradeToClose!=null){
            System.out.println("verigied  ");

            tradeToClose.setOpen(false);

            Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
            tradeToClose.setTimestampClose(timestampClose);

            CurrencyPair closingPair = thisPair(symbols);
            tradeToClose.setCurrencyPairClose(closingPair);
            iCurrencyPairService.saveCurrencyPair(closingPair);
            iTradeService.updateAndSaveTrade(tradeToClose);
        }




//        Trade trade;
//        try{
//            trade = iTradeService.findTradeById(Integer.parseInt(jsonID));
//            System.out.println("got something "+trade.toString());
//        }catch (Exception e){
//            System.out.println("got nothing");
//            e.printStackTrace();
//        }




//        System.out.println("trade is " + trade.toString());
//
//        Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
//        trade.setTimestampClose(timestampClose);
//
//        trade.setOpen(false);
//
//        CurrencyPair closingPair = thisPair(trade.getCurrencyPairOpen().getSymbols());
//        trade.setCurrencyPairClose(closingPair);
//        iCurrencyPairService.saveCurrencyPair(closingPair);
//
//
//        iTradeService.saveTrade(trade);
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

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public User findById(@RequestBody String id){

        int i = Integer.parseInt(id);
        List<User> users = iUserService.getAllUsers();

        for(User u : users){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }

    @RequestMapping(value = "/getAllTrades", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Trade> findAllTrades(){

        return iTradeService.getAllTrades();
    }

    @RequestMapping(value = "/getOpenTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> findOpenTrades(@RequestBody String userId){

        int i = Integer.parseInt(userId);
        User currentUser = iUserService.findById(i);

        List<Trade> openTrades = iTradeService.findByUser(currentUser).stream().filter(Trade::isOpen).collect(Collectors.toList());

        return openTrades;
    }



}
