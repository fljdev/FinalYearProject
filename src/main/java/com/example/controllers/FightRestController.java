package com.example.controllers;

import com.example.entities.Fight;
import com.example.forex.CurrencyPair;
import com.example.forex.ForexDriver;
import com.example.services.ICurrencyPairService;
import com.example.services.IFightService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 09/02/2017.
 */

@RestController
@RequestMapping("/api/fight")
public class FightRestController {

    IFightService iFightService;
    ICurrencyPairService iCurrencyPairService;

    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }

    @Autowired
    public void setiFightService(IFightService iFightService){
        this.iFightService = iFightService;
    }




    @RequestMapping(value = "/getFightGameObject", method = RequestMethod.POST, produces = "application/json")
    public Fight fight(@RequestBody String jsonLogin)throws Exception{

        JSONObject jsonObject = new JSONObject(jsonLogin);

        String challengerId = jsonObject.getString("cId");
        int challId = Integer.parseInt(challengerId);
        String cPair = jsonObject.getString("cPair");
        String cDir = jsonObject.getString("currUserDirection");
        String cStake = jsonObject.getString("currUserStake");
        String cLev = jsonObject.getString("currUserLeverage");

        String opponentId = jsonObject.getString("oId");
        int oppId = Integer.parseInt(opponentId);
        String oPair = jsonObject.getString("oPair");
        String oDir = jsonObject.getString("askedUserDirection");
        String oStake = jsonObject.getString("askedUserStake");
        String oLev = jsonObject.getString("askedUserLeverage");


        ArrayList<CurrencyPair> pairs = new ArrayList<>();
        pairs=allPairs();
        CurrencyPair challengerPair = new CurrencyPair();
        CurrencyPair opponentPair = new CurrencyPair();
        for(CurrencyPair cp : pairs){
            if(cp.getSymbols().equalsIgnoreCase(cPair)){
                challengerPair=cp;
                iCurrencyPairService.saveCurrencyPair(challengerPair);
            }
            if(cp.getSymbols().equalsIgnoreCase(oPair)){
                opponentPair=cp;
                iCurrencyPairService.saveCurrencyPair(opponentPair);
            }
        }
        ArrayList<CurrencyPair>temp = new ArrayList<>();
        temp.add(challengerPair);
        temp.add(opponentPair);
        Set<CurrencyPair> set = new HashSet<CurrencyPair>(temp);


        Fight aFight = new Fight();

        aFight.setChallengerID(challId);
        aFight.setPairs(set);
        aFight.setChallengerDirection(cDir);
        aFight.setChallengerStake(Double.parseDouble(cStake));
        aFight.setChallengerLeverage(Double.parseDouble(cLev));

        aFight.setOpponentID(oppId);
        aFight.setOpponentDirection(oDir);
        aFight.setOpponentStake(Double.parseDouble(oStake));
        aFight.setOpponentLeverage(Double.parseDouble(oLev));

        System.out.println(aFight.toString());
        iFightService.saveFight(aFight);


        return null;
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
}
