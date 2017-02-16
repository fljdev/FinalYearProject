package com.example.controllers;

import com.example.entities.CurrencyPair;
import com.example.entities.FightStart;
import com.example.forex.ForexDriver;
import com.example.services.ICurrencyPairService;
import com.example.services.IFightEndService;
import com.example.services.IFightStartService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 09/02/2017.
 */

@RestController
@RequestMapping("/api/fight")
public class FightRestController {

    IFightStartService iFightStartService;
    IFightEndService iFightEndService;
    ICurrencyPairService iCurrencyPairService;


    @Autowired
    public void setiFightStartService(IFightStartService iFightStartService){
        this.iFightStartService = iFightStartService;
    }

    @Autowired
    public void setiFightEndService(IFightEndService iFightEndService){
        this.iFightEndService = iFightEndService;
    }

    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }



    @RequestMapping(value = "/getAllFightStartObjects", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<FightStart> getAllFightStartObjects(){
        ArrayList<FightStart> fights = new ArrayList<>();
        fights =(ArrayList<FightStart>) iFightStartService.getAllFights();
        return fights;
    }

    @RequestMapping(value = "/findFightObjectById", method = RequestMethod.POST, produces = "application/json")
    public FightStart findById(@RequestBody String id){

        int i = Integer.parseInt(id);
        ArrayList<FightStart> fightStarts = getAllFightStartObjects();

        for(FightStart fs : fightStarts){
            if(fs.getId()== i ){

                return fs;
            }
        }
        return null;
    }



    @RequestMapping(value = "/makeFightStartObjAndReturnID", method = RequestMethod.POST, produces = "application/json")
    public int fightStart(@RequestBody String jsonLogin)throws Exception{

        JSONObject jsonObject = new JSONObject(jsonLogin);

        String challengerId = jsonObject.getString("cId");
        int challId = Integer.parseInt(challengerId);

        String cBal = jsonObject.getString("cBalance");
        double cBalance = Double.parseDouble(cBal);
        String cPair = jsonObject.getString("cPair");
        String cDir = jsonObject.getString("currUserDirection");
        String cStake = jsonObject.getString("currUserStake");
        String cLev = jsonObject.getString("currUserLeverage");

        String opponentId = jsonObject.getString("oId");
        int oppId = Integer.parseInt(opponentId);
        String oBal = jsonObject.getString("oBalance");

        double oBalance = Double.parseDouble(oBal);
        String oPair = jsonObject.getString("oPair");


        String oDir = jsonObject.getString("askedUserDirection");
//        String oStake = jsonObject.getString("askedUserStake");
        String oLev = jsonObject.getString("askedUserLeverage");


        ArrayList<CurrencyPair> pairs = new ArrayList<>();
        pairs=allPairs();

        List<CurrencyPair> cPairs = new ArrayList<CurrencyPair>();
//        CurrencyPair challengerPair = new CurrencyPair();
//        CurrencyPair opponentPair = new CurrencyPair();
        for(CurrencyPair cp : pairs){
            if(cp.getSymbols().equalsIgnoreCase(cPair)){
                CurrencyPair challengerPair=cp;
                iCurrencyPairService.saveCurrencyPair(challengerPair);
                cPairs.add(cp);
            }
            if(cp.getSymbols().equalsIgnoreCase(oPair)){
                CurrencyPair opponentPair=cp;
                iCurrencyPairService.saveCurrencyPair(opponentPair);
                cPairs.add(cp);
            }
        }


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        FightStart aFightStart = new FightStart();

        aFightStart.setTimestamp(timestamp);

        aFightStart.setChallengerBalance(cBalance);
        aFightStart.setChallengerID(challId);
        aFightStart.setPairs(cPairs);
        aFightStart.setChallengerDirection(cDir);
        aFightStart.setChallengerStake(Double.parseDouble(cStake));
        aFightStart.setChallengerLeverage(Double.parseDouble(cLev));

        aFightStart.setOpponentBalance(oBalance);
        aFightStart.setOpponentID(oppId);
        aFightStart.setOpponentDirection(oDir);
        aFightStart.setOpponentStake(Double.parseDouble(cStake));
        aFightStart.setOpponentLeverage(Double.parseDouble(oLev));

        iFightStartService.saveFightStart(aFightStart);


        int fightStartObjID = aFightStart.getId();
        return fightStartObjID;
    }

//    @RequestMapping(value = "/getFightGameObject", method = RequestMethod.POST, produces = "application/json")
//    public Fight fight(@RequestBody String jsonLogin)throws Exception{
//
//        JSONObject jsonObject = new JSONObject(jsonLogin);
//
//        String challengerId = jsonObject.getString("cId");
//        int challId = Integer.parseInt(challengerId);
//        String cPair = jsonObject.getString("cPair");
//        String cDir = jsonObject.getString("currUserDirection");
//        String cStake = jsonObject.getString("currUserStake");
//        String cLev = jsonObject.getString("currUserLeverage");
//
//        String opponentId = jsonObject.getString("oId");
//        int oppId = Integer.parseInt(opponentId);
//        String oPair = jsonObject.getString("oPair");
//        String oDir = jsonObject.getString("askedUserDirection");
//        String oStake = jsonObject.getString("askedUserStake");
//        String oLev = jsonObject.getString("askedUserLeverage");
//
//
//        ArrayList<CurrencyPair> pairs = new ArrayList<>();
//        pairs=allPairs();
//        CurrencyPair challengerPair = new CurrencyPair();
//        CurrencyPair opponentPair = new CurrencyPair();
//        for(CurrencyPair cp : pairs){
//            if(cp.getSymbols().equalsIgnoreCase(cPair)){
//                challengerPair=cp;
//                iCurrencyPairService.saveCurrencyPair(challengerPair);
//            }
//            if(cp.getSymbols().equalsIgnoreCase(oPair)){
//                opponentPair=cp;
//                iCurrencyPairService.saveCurrencyPair(opponentPair);
//            }
//        }
//        ArrayList<CurrencyPair>temp = new ArrayList<>();
//        temp.add(challengerPair);
//        temp.add(opponentPair);
//        Set<CurrencyPair> set = new HashSet<CurrencyPair>(temp);
//
//
//        Fight aFight = new Fight();
//
//        aFight.setChallengerID(challId);
//        aFight.setPairs(set);
//        aFight.setChallengerDirection(cDir);
//        aFight.setChallengerStake(Double.parseDouble(cStake));
//        aFight.setChallengerLeverage(Double.parseDouble(cLev));
//
//        aFight.setOpponentID(oppId);
//        aFight.setOpponentDirection(oDir);
//        aFight.setOpponentStake(Double.parseDouble(oStake));
//        aFight.setOpponentLeverage(Double.parseDouble(oLev));
//
//        System.out.println(aFight.toString());
//        iFightService.saveFight(aFight);
//
//
//        return aFight;
//    }

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


}
