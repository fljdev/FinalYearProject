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
        double position = jsonObject.getDouble("position");

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
        trade.setPosition(position);

        iTradeService.saveTrade(trade);

        return trade;
    }//end saveTrade

    @RequestMapping(value ="/updateEachTrade", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String live(@RequestBody String json)throws Exception{

        /**
         * Finding the trade will give me the user, which will allow me
         * to find all the open trades for that user
         */
        JSONObject jsonObject = new JSONObject(json);
        int tradeID = jsonObject.getInt("id");
        String id=String.valueOf(tradeID);

        Trade thisTrade = iTradeService.findTradeById(tradeID);
        User user = thisTrade.getUser();
        System.out.println("user is "+user.getUsername());
        String idString = String.valueOf(user.getId());

        List<Trade>open = findOpenTrades(idString);
        System.out.println("open trades is "+open.size());

        List<LiveTradeInfo> liveTradeInfoList = thisTrade.getLiveTradeInfoList();

        LiveTradeInfo liveTradeInfo;

        for(Trade t : open){
            liveTradeInfo = new LiveTradeInfo();

            CurrencyPair thisCurrencyPair = thisPair(t.getCurrencyPairOpen().getSymbols());

            Timestamp tickTime = new Timestamp(System.currentTimeMillis());

            liveTradeInfo.setTradeID(t.getId());
            System.out.println("t.getId() is "+t.getId());
            liveTradeInfo.setTickTime(tickTime);

            liveTradeInfo.setCurrentAsk(thisCurrencyPair.getAsk());
            liveTradeInfo.setCurrentBid(thisCurrencyPair.getBid());

            liveTradeInfoList.add(liveTradeInfo);


            calcThisProfitAndLoss(t,thisCurrencyPair);
            iLiveTradeInfo.saveLiveTradeInfo(liveTradeInfo);

            Thread.sleep(50);
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




        return id;
    }

    private double calcThisProfitAndLoss(Trade t, CurrencyPair cp){

        System.out.println("inside calcThisProfitAndLoss with  "+t.toString());
        CurrencyPair thisPair = cp;
        Trade thisTrade = t;




        return 0;
    }

    @RequestMapping(value = "/closeLiveTrade", method = RequestMethod.POST)
    public void closeThisTrade(@RequestBody String json)throws Exception{

        System.out.println("testing got here");
        JSONObject jsonObject = new JSONObject(json);
        String playerID = jsonObject.getString("userID");
        String pairSymbols = jsonObject.getString("symbols");
//        String profitString = jsonObject.getString("profitAndLoss");
//        double profit = Double.parseDouble(profitString);



        CurrencyPair closingPair = thisPair(pairSymbols);
        iCurrencyPairService.saveCurrencyPair(closingPair);


        List<Trade> openTrades = findOpenTrades(playerID);

        for(Trade t : openTrades){
            if(t.getCurrencyPairOpen().getSymbols().equalsIgnoreCase(pairSymbols)){
                Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
                t.setTimestampClose(timestampClose);
                t.setCurrencyPairClose(closingPair);
//                t.setClosingProfitLoss(profit);
                iTradeService.saveTrade(t);

//                User user = findById(playerID);
//                BankAccount bankAccount = user.getAccount();
//                double marginPayed = t.getMargin();
//                double currentBalance = bankAccount.getBalance();
//                double updatedBalance = currentBalance+marginPayed;
//                updatedBalance +=profit;
//                bankAccount.setBalance(updatedBalance);
//                iBankAccountService.register(bankAccount);
//
//                iUserService.register(user);
            }
        }
    }






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
        List<User> users = iUserService.getAllUsers();
        User currentUser = null;
        for(User u : users){
            if(u.getId()== i ){
                currentUser = u;
            }
        }

        List<Trade> allTrades= findAllTrades();
        List<Trade> openTrades = new ArrayList<>();

        for(Trade trade : allTrades){
            if((trade.getUser().getId()== currentUser.getId()) && (trade.getTimestampClose()==null)){
                openTrades.add(trade);
            }
        }
        return openTrades;
    }



}
