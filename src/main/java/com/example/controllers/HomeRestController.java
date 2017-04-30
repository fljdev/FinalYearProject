package com.example.controllers;

import com.example.entities.*;
import com.example.services.*;
import com.google.common.collect.Multiset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by admin on 26/04/2017.
 */
@RestController
@RequestMapping("/api/home")
public class HomeRestController {

    IChallengeService iChallengeService;
    IUserService iUserService;
    IGameAccountService iGameAccountService;
    IResultService iResultService;
    ITradeService iTradeService;
    ICurrencyPairService iCurrencyPairService;
    IBankAccountService iBankAccountService;
    @Autowired
    public void setiBankAccountService(IBankAccountService service){
        this.iBankAccountService=service;
    }
    @Autowired
    public void setiTradeService(ITradeService iTradeService){
        this.iTradeService = iTradeService;
    }
    @Autowired
    public void setiResultService(IResultService service){
        this.iResultService = service;
    }
    @Autowired
    public void setiGameAccountService(IGameAccountService iGameAccountService){
        this.iGameAccountService = iGameAccountService;
    }
    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }
    @Autowired
    public void setiChallengeService(IChallengeService service){

        this.iChallengeService = service;
    }
    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }

    @RequestMapping(value = "/averageBalance", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Double> getAverageAccountBalance() {
        List<Double>accountBalances=new ArrayList<>();
        for(BankAccount b : iBankAccountService.findAllAccounts()){
            System.out.println("adding this balance: ");
            accountBalances.add(b.getBalance());
        }
        return accountBalances;
    }

    @RequestMapping(value = "/averageSoloTrade", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public double averageSoloTrade() {

        double total = 0;
        for(Trade t: iTradeService.getAllTrades()){
            total+=t.getPositionUnits();
        }
        double average = total / iTradeService.getAllTrades().size();
        return average;
    }

    @RequestMapping(value = "/largestSoloTradeByAllUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public double largestSoloTradeByAllUsers() {

        ArrayList<Double>allTrades = new ArrayList<>();
        for(Trade t : iTradeService.getAllTrades()){
            allTrades.add(t.getPositionUnits());
        }


        return Collections.max(allTrades);
    }

    @RequestMapping(value = "/numberOfSoloTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double getNumberOfSoloTrades(@RequestBody int id) {
        User user = iUserService.findById(id);

        List<Trade> soloTrades = iTradeService.findByUser(user);

        double soloTradeCount = 0;
        for(Trade t : soloTrades){
            if(t.getChallenge()==null){
                soloTradeCount++;
            }}
        return soloTradeCount;
    }
    @RequestMapping(value = "/soloTradeProfit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double soloTradeProfit(@RequestBody int id) {
        User user = iUserService.findById(id);
        double soloTradeProfit=0;
        for (Trade t : iTradeService.findByUser(user)){
            if(t.getClosingProfitLoss()>0){
                soloTradeProfit+=t.getClosingProfitLoss();
            }
        }
        return soloTradeProfit;
    }
    @RequestMapping(value = "/soloTradeLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double soloTradeLoss(@RequestBody int id) {
        User user = iUserService.findById(id);
        double soloTradeLoss=0;
        for (Trade t : iTradeService.findByUser(user)){
            if(t.getClosingProfitLoss()<0){
                soloTradeLoss+=t.getClosingProfitLoss();
            }
        }
        return soloTradeLoss;
    }

    @RequestMapping(value = "/largestSoloTrade", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double largestSoloTrade(@RequestBody int id) {
        Set<Double> tradeAmounts = new HashSet<>();
        for (Trade t : iTradeService.findByUser(iUserService.findById(id))){
          tradeAmounts.add(t.getPositionUnits());
        }
        return Collections.max(tradeAmounts);
    }

    @RequestMapping(value = "/favPair", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String favPair(@RequestBody int id) {
        List<String>pairs=new ArrayList<>();

        for (Trade t : iTradeService.findByUser(iUserService.findById(id))){
            pairs.add(t.getCurrencyPairOpen().getSymbols());
        }

        int hightest = 0;

        for(String t : pairs){

        }

        return "";
    }



    @RequestMapping(value = "/numberOfGameTrades", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double getNumberOfGameTrades(@RequestBody int id) {
        User user = iUserService.findById(id);

        List<Trade> gameTrades = iTradeService.findByUser(user);

        double gameTradeCount = 0;
        for(Trade t : gameTrades){
            if(t.getChallenge()!=null){
                gameTradeCount++;
            }}

        return gameTradeCount;
    }

    @RequestMapping(value = "/getAllChallengesUserInvolvedIn", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double getAllChallengesUserInvolvedIn(@RequestBody int id) {
        User user = iUserService.findById(id);
        return iChallengeService.getAllChallengesUserInvolvedIn(user).size();
    }

    @RequestMapping(value = "/challengesWithdrawn", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double challengesWithdrawn(@RequestBody int id) {
        User user = iUserService.findById(id);
        List<Challenge>withdrawn = new ArrayList<>();
        for(Challenge c :  iChallengeService.getAllChallengesUserInvolvedIn(user)){
            if(c.isWithdrawen()){
                withdrawn.add(c);
            }
        }
        return  withdrawn.size();
    }

    @RequestMapping(value = "/challengesDeclined", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double challengesDeclined(@RequestBody int id) {
        User user = iUserService.findById(id);
        List<Challenge>declined = new ArrayList<>();
        for(Challenge c :  iChallengeService.getAllChallengesUserInvolvedIn(user)){
            if(c.isDeclined()){
                declined.add(c);
            }
        }
        return  declined.size();
    }

    @RequestMapping(value = "/challengesAccepted", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double challengesAccepted(@RequestBody int id) {
        User user = iUserService.findById(id);
        List<Challenge>accepted = new ArrayList<>();
        for(Challenge c :  iChallengeService.getAllChallengesUserInvolvedIn(user)){
            if(c.isAccepted()){
                accepted.add(c);
            }
        }
        return  accepted.size();
    }



    @RequestMapping(value = "/gameProfit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double gameProfit(@RequestBody int id) {
        User user = iUserService.findById(id);
        double win=0;
        for(Result r : iResultService.findByUser(user)){
            if(r.getWinner()==user){
                win+=r.getPrize();
            }
        }
        return win;
    }


    @RequestMapping(value = "/gameLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public double gameLoss(@RequestBody int id) {
        User user = iUserService.findById(id);
        double loss=0;
        for(Result r : iResultService.findByUser(user)){
            if(r.getLoser()==user){
                loss+=r.getPrize();
            }
        }
        return loss;
    }



}
