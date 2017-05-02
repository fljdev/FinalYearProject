package com.example.controllers;

import com.example.entities.*;
import com.example.services.IChallengeService;
import com.example.services.IGameAccountService;
import com.example.services.IResultService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestContoller {

    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");


    IChallengeService iChallengeService;
    IUserService iUserService;
    IGameAccountService iGameAccountService;
    IResultService iResultService;

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



//    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
//    public User findById(@RequestBody String id){
//        return iUserService.findById(Integer.parseInt(id));
//    }



    @RequestMapping(value = "/findChallengeById", method = RequestMethod.POST, produces = "application/json")
    public Challenge findChallengeById(@RequestBody String id){
        return iChallengeService.findById(Integer.parseInt(id));
    }

    @RequestMapping(value = "/withdrawChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Challenge withdrawChallenge(@RequestBody String id){
        Challenge challengeToWithdraw=iChallengeService.findById(Integer.parseInt(id));
        challengeToWithdraw.setWithdrawen(true);
        challengeToWithdraw.setOpen(false);
        iChallengeService.saveChallenge(challengeToWithdraw);

        return challengeToWithdraw;
    }//end withdrawChallenge


    @RequestMapping(value = "/withdrawMyChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void withdrawMyChallenge(@RequestBody int id){
        User user = iUserService.findById(id);


        Challenge challengeToWithdraw = null;

        for(Challenge c : iChallengeService.getAllChallenges()){
            if(c.getChallenger()==user){
                challengeToWithdraw=c;
            }
        }
        challengeToWithdraw.setWithdrawen(true);
        challengeToWithdraw.setOpen(false);
        iChallengeService.saveChallenge(challengeToWithdraw);
    }//end withdrawChallenge


    @RequestMapping(value = "/declineChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Challenge declineChallenge(@RequestBody int id){

        Challenge toDecline = iChallengeService.findById(id);
        toDecline.setDeclined(true);
        toDecline.setOpen(false);
        iChallengeService.saveChallenge(toDecline);
        return toDecline;
    }


    @RequestMapping(value = "/acceptChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void acceptChallenge(@RequestBody int id){

        Challenge challengeToAccept = iChallengeService.findById(id);

        User challenger = challengeToAccept.getChallenger();
        challenger.setBusy(true);
        iUserService.register(challenger);


        User opponent = challengeToAccept.getOpponent();
        opponent.setBusy(true);
        iUserService.register(opponent);

        challengeToAccept.setAccepted(true);
        challengeToAccept.setOpen(false);
        iChallengeService.saveChallenge(challengeToAccept);
    }


    @RequestMapping(value ="/challengesSent", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Challenge> getAllSentChallenges(@RequestBody User user){
        return iChallengeService.getAllChallengesSent(user);
    }



    @RequestMapping(value ="/challengesRecieved", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Challenge> getAllRecievedChallenges(@RequestBody User user){
        System.out.println("service call gets "+iChallengeService.getAllChallengesRecieved(user));
        return iChallengeService.getAllChallengesRecieved(user);
    }

    @RequestMapping(value ="/liveChallenge", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Challenge> liveChallenge(@RequestBody User user){
        List<Challenge>live =new ArrayList<>();
        for(Challenge c :iChallengeService.getAllChallengesRecieved(user) ){
            if(c.isOpen()){
                live.add(c);
            }
        }
        return live ;
    }




    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST, produces = "application/json")
    public Challenge saveThisChallenge(@RequestBody String params){
        JSONObject jsonObject = new JSONObject(params);

        Challenge thisChallenge = new Challenge();
        User currUserObject =iUserService.findById(Integer.parseInt(jsonObject.getString("currUserID")));
        User oppUserObject = iUserService.findById(Integer.parseInt(jsonObject.getString("opponentID")));

        int duration = Integer.parseInt(jsonObject.getString("duration"));
        double stake = Double.parseDouble(jsonObject.getString("stake"));

        thisChallenge.setChallenger(currUserObject);
        thisChallenge.setOpponent(oppUserObject);
        thisChallenge.setDuration(duration);
        thisChallenge.setStake(stake);
        thisChallenge.setOpen(true);

        thisChallenge.setChallengeRequestValidTime(60);
        thisChallenge.setGameTime(duration*60);

        iChallengeService.saveChallenge(thisChallenge);

        return thisChallenge;
    }

    @RequestMapping(value = "/getChallengeRequestValidTimeForChallenger", method = RequestMethod.POST, produces = "application/json")
    public int getChallengeRequestValidTimeForChallenger(@RequestBody int id){

        Challenge c = iChallengeService.findById(id);
        int challengeRequestValidTimeRemaining = c.getChallengeRequestValidTime();
        int updatedTime = challengeRequestValidTimeRemaining-5;
        c.setChallengeRequestValidTime(updatedTime);
        iChallengeService.saveChallenge(c);
        return challengeRequestValidTimeRemaining;

    }


    @RequestMapping(value = "/getChallengeRequestValidTimeForOpponent", method = RequestMethod.POST, produces = "application/json")
    public int getChallengeRequestValidTimeForOpponent(@RequestBody int id){
        return iChallengeService.findById(id).getChallengeRequestValidTime();
    }



    @RequestMapping(value = "/gameTimeRemainingForChallenger", method = RequestMethod.POST, produces = "application/json")
    public int gameTimeRemaining(@RequestBody int id){
        Challenge c = iChallengeService.findById(id);
        int timeRemaining = c.getGameTime();
        int updatedTime = timeRemaining-5;
        c.setGameTime(updatedTime);
        iChallengeService.saveChallenge(c);
        return updatedTime;
    }

    @RequestMapping(value = "/gameTimeRemainingForOpponent", method = RequestMethod.POST, produces = "application/json")
    public int gameTimeRemainingForOpponent(@RequestBody int id){
        return iChallengeService.findById(id).getGameTime();
    }



    @RequestMapping(value = "/updateGameAccount",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void updateGameAccount(@RequestBody String id){
        Challenge thisChallenge=iChallengeService.findById(Integer.parseInt(id));

        User challenger = thisChallenge.getChallenger();
        GameAccount challengerGameAccount = new GameAccount();
        challengerGameAccount.setBalance(thisChallenge.getStake()*200);
        challengerGameAccount.setUser(challenger);
        challengerGameAccount.setChallenge(thisChallenge);
        iGameAccountService.register(challengerGameAccount);
        iUserService.register(challenger);


        User opponent = thisChallenge.getOpponent();
        GameAccount opponentGameAccount = new GameAccount();
        opponentGameAccount.setBalance(thisChallenge.getStake()*200);
        opponentGameAccount.setUser(opponent);
        opponentGameAccount.setChallenge(thisChallenge);
        iGameAccountService.register(opponentGameAccount);
        iUserService.register(opponent);
    }



    @RequestMapping(value = "/waitForReply", method = RequestMethod.POST, produces = "application/json")
    public Challenge waitForReply(@RequestBody String id){
        return iChallengeService.findById(Integer.parseInt(id));
    }



    @RequestMapping(value ="/completeChallenge", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User completeChallenge(@RequestBody String id){
        Challenge challenge = iChallengeService.findById(Integer.parseInt(id));

        User winner;
        User loser;

        User opponent = challenge.getOpponent();
        opponent.setBusy(false);

        User challenger = challenge.getChallenger();
        challenger.setBusy(false);



        double challBal = iGameAccountService.findGameAccountByUserAndChallenge(challenger,challenge).getBalance();
        double oppBal = iGameAccountService.findGameAccountByUserAndChallenge(opponent,challenge).getBalance();

        if(challBal>oppBal){
            winner = challenger;
            loser = opponent;
        }else{
            winner=opponent;
            loser=challenger;
        }




        Result result = new Result();
        double prize = challenge.getStake();

        result.setWinner(winner);
        result.setLoser(loser);
        result.setPrize(prize);

        result.setChallenge(challenge);
        iResultService.saveResult(result);


        opponent.setGameMargin(0);
        opponent.setGameProfit(0);
        challenger.setGameMargin(0);
        challenger.setGameProfit(0);

        iUserService.register(opponent);
        iUserService.register(challenger);

        return winner;
    }
}
