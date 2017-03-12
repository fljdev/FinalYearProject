package com.example.controllers;

import com.example.entities.*;
import com.example.forex.ForexDriver;
import com.example.services.IBankAccountService;
import com.example.services.ICurrencyPairService;
import com.example.services.ITradeService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;



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
    public void saveThisTrade(@RequestBody String json)throws Exception{
        JSONObject jsonObject = new JSONObject(json);

        String playerID = jsonObject.getString("playerID");
        String pairSymbols = jsonObject.getString("pairSymbols");
        String marginString = jsonObject.getString("margin");
        double margin = Double.parseDouble(marginString);
        String action = jsonObject.getString("action");

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

        iTradeService.saveTrade(trade);
    }//end saveTrade

    @RequestMapping(value = "/closeTrade", method = RequestMethod.POST)
    public void closeThisTrade(@RequestBody String json)throws Exception{
        JSONObject jsonObject = new JSONObject(json);
        String playerID = jsonObject.getString("id");
        String pairSymbols = jsonObject.getString("sym");
        String profitString = jsonObject.getString("profitAndLoss");
        double profit = Double.parseDouble(profitString);



        CurrencyPair closingPair = thisPair(pairSymbols);
        iCurrencyPairService.saveCurrencyPair(closingPair);

        ArrayList<Trade> openTrades = findOpenTrades(playerID);
        for(Trade t : openTrades){
            if(t.getCurrencyPairOpen().getSymbols().equalsIgnoreCase(pairSymbols)){
                Timestamp timestampClose = new Timestamp(System.currentTimeMillis());
                t.setTimestampClose(timestampClose);
                t.setCurrencyPairClose(closingPair);
                t.setProfitLoss(profit);
                iTradeService.saveTrade(t);

                User user = findById(playerID);
                BankAccount bankAccount = user.getAccount();
                double marginPayed = t.getMargin();
                double currentBalance = bankAccount.getBalance();
                double updatedBalance = currentBalance+marginPayed;
                updatedBalance +=profit;
                bankAccount.setBalance(updatedBalance);
                iBankAccountService.register(bankAccount);

                iUserService.register(user);
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
        ArrayList<User> users = iUserService.getAllUsers();

        for(User u : users){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }

    @RequestMapping(value = "/getAllTrades", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<Trade> findAllTrades(){
        ArrayList<Trade> trades = new ArrayList<>();
        trades=    iTradeService.getAllTrades();
        return trades;
    }

    @RequestMapping(value = "/getOpenTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ArrayList<Trade> findOpenTrades(@RequestBody String userId){

        int i = Integer.parseInt(userId);
        ArrayList<User> users = iUserService.getAllUsers();
        User currentUser = null;
        for(User u : users){
            if(u.getId()== i ){
                currentUser = u;
            }
        }

        ArrayList<Trade> allTrades= findAllTrades();
        ArrayList<Trade> openTrades = new ArrayList<>();

        for(Trade trade : allTrades){
            if((trade.getUser().getId()== currentUser.getId()) && (trade.getTimestampClose()==null)){
                openTrades.add(trade);
            }
        }
        return openTrades;
    }



}
