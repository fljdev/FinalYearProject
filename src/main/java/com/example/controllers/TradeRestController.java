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


@RestController
@RequestMapping("/api/trade")
public class TradeRestController {

    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IUserService iUserService;
    IBankAccountService iBankAccountService;

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

    @RequestMapping(value = "/closeLiveTrade", method = RequestMethod.POST)
    public Trade closeThisTrade(@RequestBody String closeParams)throws Exception {

        JSONObject jsonObject = new JSONObject(closeParams);
        String symbols = jsonObject.getString("sym");

        User user = iUserService.findById(Integer.parseInt(jsonObject.getString("id")));
        Trade tradeToClose = iTradeService.findBySymbols(symbols,user);

        if(tradeToClose!=null){
            tradeToClose.setOpen(false);

            BankAccount bankAccount = user.getAccount();
            double currentBalance = bankAccount.getBalance();
            double balanceUpdate = currentBalance + tradeToClose.getMargin() + tradeToClose.getClosingProfitLoss();
            bankAccount.setBalance(balanceUpdate);
            iBankAccountService.register(bankAccount);


            Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
            tradeToClose.setTimestampClose(timestampClose);

            CurrencyPair closingPair = thisPair(symbols);
            tradeToClose.setCurrencyPairClose(closingPair);

            iCurrencyPairService.saveCurrencyPair(closingPair);
            iTradeService.updateAndSaveTrade(tradeToClose);



            double currProfit = user.getCurrentProfit();
            double thisTradePL = tradeToClose.getClosingProfitLoss();
            currProfit -= thisTradePL;
            user.setCurrentProfit(currProfit);

            double existingMargin = user.getTotalMargin();
            double updatedMargin = existingMargin - tradeToClose.getMargin();
            user.setTotalMargin(updatedMargin);
            iUserService.register(user);
        }
        return tradeToClose;
    }





    @RequestMapping(value ="/watchForChanges", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User watchChanges(@RequestBody String userJson)throws Exception{
        JSONObject jsonObject = new JSONObject(userJson);
        live(userJson);
        totalProfit(userJson);
        User user = iUserService.findById(jsonObject.getInt("id"));
        return user;
    }




    @RequestMapping(value ="/updateEachTrade", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> live(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));
        List<Trade> openTradesWithoutChallenges = new ArrayList<>();
        if(user!=null){

            List<Trade> openTrades = iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList());
            for(Trade t : openTrades){
                if(t.getChallenge()==null){
                    openTradesWithoutChallenges.add(t);
                }
            }


            for(Trade t : openTradesWithoutChallenges){

                CurrencyPair thisCurrencyPair = thisPair(t.getCurrencyPairOpen().getSymbols());

                double profit = calcThisProfitAndLoss(t,thisCurrencyPair);
                System.out.println("in updateEachTrade and profit is "+profit);
                System.out.println("profit is "+profit);
                t.setClosingProfitLoss(profit);

                iTradeService.saveTrade(t);
            }
        }
        return openTradesWithoutChallenges;
    }


    @RequestMapping(value ="/getTotalProfitAndLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User totalProfit(@RequestBody String userJson)throws Exception{

        JSONObject jsonObject = new JSONObject(userJson);

        User user = iUserService.findById(jsonObject.getInt("id"));

        double totalProfit=0;

        if(user!=null){
            List<Trade> openTrades = iTradeService.findByUser(user).stream().filter(Trade::isOpen).collect(Collectors.toList());



            List<Trade> openTradesWithoutChallenges = new ArrayList<>();
            for(Trade t : openTrades){
                if(t.getChallenge()==null){
                    openTradesWithoutChallenges.add(t);
                }
            }

            for(Trade t : openTradesWithoutChallenges){
                totalProfit += t.getClosingProfitLoss();
                user.setCurrentProfit(totalProfit);

                iUserService.register(user);
            }
        }
        return user;
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

        List<Trade>openTrades = iTradeService.findByUser(currentUser).stream().filter(Trade::isOpen).collect(Collectors.toList());

        List<Trade> openTradesWithoutChallenges = new ArrayList<>();
        for(Trade t : openTrades){
            if(t.getChallenge()==null){
                openTradesWithoutChallenges.add(t);
            }
        }
        return openTradesWithoutChallenges;
    }

    @RequestMapping(value = "/findTradesByUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Trade> findTradesByUser(@RequestBody String id){
        System.out.println("id is "+id);
        User currentUser = iUserService.findById(Integer.parseInt(id));
        return iTradeService.findByUser(currentUser);
    }




}
