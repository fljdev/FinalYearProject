package com.example.controllers;

import com.example.entities.BankAccount;
import com.example.entities.Challenge;
import com.example.entities.GameAccount;
import com.example.entities.User;
import com.example.services.IChallengeService;
import com.example.services.IGameAccountService;
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



    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public User findById(@RequestBody String id){
        return iUserService.findById(Integer.parseInt(id));
    }



    @RequestMapping(value = "/findChallengeById", method = RequestMethod.POST, produces = "application/json")
    public Challenge findChallengeById(@RequestBody String id){
        return iChallengeService.findById(Integer.parseInt(id));
    }

    @RequestMapping(value = "/withdrawChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void withdrawChallenge(@RequestBody String id){
        Timestamp challengeWithdrawenMilli = new Timestamp(System.currentTimeMillis());

        Challenge challengeToWithdraw=iChallengeService.findById(Integer.parseInt(id));

        challengeToWithdraw.setWithdrawen(true);
        challengeToWithdraw.setChallengeWithdrawen(sdf.format(challengeWithdrawenMilli));
        challengeToWithdraw.setOpen(false);

        iChallengeService.saveChallenge(challengeToWithdraw);
    }//end withdrawChallenge


    @RequestMapping(value = "/declineChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void declineChallenge(@RequestBody String id){
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());

        Challenge challengeToDecline=iChallengeService.findById(Integer.parseInt(id));

        challengeToDecline.setDeclined(true);
        challengeToDecline.setOpen(false);
        challengeToDecline.setChallengeDeclined(sdf.format(challengeSentMili));

        iChallengeService.saveChallenge(challengeToDecline);
    }


    @RequestMapping(value = "/acceptChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void acceptChallenge(@RequestBody String id){
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());

        Challenge challengeToAccept = iChallengeService.findById(Integer.parseInt(id));

        challengeToAccept.setAccepted(true);
        challengeToAccept.setOpen(true);
        challengeToAccept.setChallengeAccepted(sdf.format(challengeSentMili));

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
        return iChallengeService.getAllChallengesRecieved(user);
    }




    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST, produces = "application/json")
    public Challenge saveThisChallenge(@RequestBody String params){
        JSONObject jsonObject = new JSONObject(params);
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());

        Challenge thisChallenge = new Challenge();
        User currUserObject =iUserService.findById(Integer.parseInt(jsonObject.getString("currUserID")));
        User oppUserObject = iUserService.findById(Integer.parseInt(jsonObject.getString("opponentID")));

        int duration = Integer.parseInt(jsonObject.getString("duration"));
        double stake = Double.parseDouble(jsonObject.getString("stake"));

        thisChallenge.setChallengerId(currUserObject.getId());
        thisChallenge.setChallengerName(currUserObject.getUsername());

        thisChallenge.setOpponentId(oppUserObject.getId());
        thisChallenge.setOpponentName(oppUserObject.getUsername());

        thisChallenge.setChallengeSent(sdf.format(challengeSentMili));

        thisChallenge.setDuration(duration);
        thisChallenge.setStake(stake);

        thisChallenge.setOpen(true);

        iChallengeService.saveChallenge(thisChallenge);

        return thisChallenge;
    }


    @RequestMapping(value = "/waitForReply", method = RequestMethod.POST, produces = "application/json")
    public Challenge waitForReply(@RequestBody String id){
        Challenge challenge = iChallengeService.findById(Integer.parseInt(id));

        return challenge;
    }




    @RequestMapping(value = "/updateGameAccount",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void updateGameAccount(@RequestBody String id){
        Challenge thisChallenge=iChallengeService.findById(Integer.parseInt(id));

        User challenger = findById(String.valueOf(thisChallenge.getChallengerId()));
        User opponent = findById(String.valueOf(thisChallenge.getOpponentId()));

        GameAccount challengerGameAccount = new GameAccount();
        challengerGameAccount.setUser(challenger);
        challengerGameAccount.setBalance(thisChallenge.getStake());
        iGameAccountService.register(challengerGameAccount);
        iUserService.register(challenger);

        GameAccount opponentGameAccount = new GameAccount();
        opponentGameAccount.setUser(opponent);
        opponentGameAccount.setBalance(thisChallenge.getStake());
        iGameAccountService.register(opponentGameAccount);
        iUserService.register(opponent);
    }






}
