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



@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestContoller {

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

        int i = Integer.parseInt(id);
        ArrayList<User> users = iUserService.getAllUsers();

        for(User u : users){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }

    @RequestMapping(value = "/findChallengeById", method = RequestMethod.POST, produces = "application/json")
    public Challenge findChallengeById(@RequestBody String id){

        int i = Integer.parseInt(id);
        ArrayList<Challenge> challenges = iChallengeService.getAllChallenges();

        for(Challenge c : challenges){
            if(c.getId()== i ){
                return c;
            }
        }
        return null;
    }

    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST, produces = "application/json")
    public void saveThisChallenge(@RequestBody String params){
        JSONObject jsonObject = new JSONObject(params);

        System.out.println(jsonObject.toString());

        String currIDString = jsonObject.getString("currUserID");
        int currID = Integer.parseInt(currIDString);
        String opponentID = jsonObject.getString("opponentID");
        int oppID = Integer.parseInt(opponentID);

        User currUserObject = findById(String.valueOf(currID));
        String currUsername =currUserObject.getUsername();

        User oppUserObject = findById(String.valueOf(oppID));
        String oppUsername = oppUserObject.getUsername();

        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());


        String durationString = jsonObject.getString("duration");
        int duration = Integer.parseInt(durationString);

        String stakeString = jsonObject.getString("stake");
        double stake = Double.parseDouble(stakeString);


        Challenge thisChallenge = new Challenge();

        thisChallenge.setChallengerId(currID);
        thisChallenge.setChallengerName(currUsername);

        thisChallenge.setOpponentId(oppID);
        thisChallenge.setOpponentName(oppUsername);

        thisChallenge.setChallengeSent(sdf.format(challengeSentMili));

        thisChallenge.setDuration(duration);
        thisChallenge.setStake(stake);

        thisChallenge.setOpen(true);

        iChallengeService.saveChallenge(thisChallenge);
    }//end saveThidChallenge


    @RequestMapping(value ="/challengesSent", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ArrayList<Challenge> getAllSentChallenges(@RequestBody String id){

        int idNo =Integer.parseInt(id);
        ArrayList<Challenge>challenges = new ArrayList();
        challenges=(ArrayList<Challenge>) iChallengeService.getAllChallenges();

        ArrayList<Challenge>sentChallenges = new ArrayList<>();

        for(Challenge c : challenges){
            if(c.getChallengerId()==idNo){
                sentChallenges.add(c);
            }
        }
        return sentChallenges;
    }

    @RequestMapping(value ="/challengesRecieved", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ArrayList<Challenge> getAllRecievedChallenges(@RequestBody String id){

        int idNo =Integer.parseInt(id);
        ArrayList<Challenge>challenges = new ArrayList();
        challenges=(ArrayList<Challenge>) iChallengeService.getAllChallenges();
        ArrayList<Challenge>challengesRecieved = new ArrayList<>();

        for(Challenge c : challenges){
            if(c.getOpponentId()==idNo){
                challengesRecieved.add(c);
            }
        }

        return challengesRecieved;
    }

    @RequestMapping(value = "/withdrawChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void withdrawChallenge(@RequestBody String id){

        int idNo = Integer.parseInt(id);

         ArrayList<Challenge>challenges = new ArrayList<>();
         challenges=(ArrayList<Challenge>) iChallengeService.getAllChallenges();

         Challenge challengeToWithdraw=null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        Timestamp challengeWithdrawenMilli = new Timestamp(System.currentTimeMillis());

        for(Challenge ch:challenges){
            if(ch.getId()==idNo){
                challengeToWithdraw = ch;

                challengeToWithdraw.setWithdrawen(true);
                challengeToWithdraw.setChallengeWithdrawen(sdf.format(challengeWithdrawenMilli));
                challengeToWithdraw.setOpen(false);

                iChallengeService.saveChallenge(challengeToWithdraw);
            }//end if
        }//end for
    }//end withdrawChallenge


    @RequestMapping(value = "/declineChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void declineChallenge(@RequestBody String id){

        int idNo = Integer.parseInt(id);

        ArrayList<Challenge>challenges = new ArrayList<>();
        challenges=(ArrayList<Challenge>) iChallengeService.getAllChallenges();

        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());

        Challenge challengeToDecline=null;

        for(Challenge chall:challenges){
            if(chall.getId()==idNo){
                challengeToDecline = chall;

                challengeToDecline.setDeclined(true);
                challengeToDecline.setOpen(false);
                challengeToDecline.setChallengeDeclined(sdf.format(challengeSentMili));

                iChallengeService.saveChallenge(challengeToDecline);
            }
        }
    }

    @RequestMapping(value = "/acceptChallenge",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void acceptChallenge(@RequestBody String id){

        int idNo = Integer.parseInt(id);

        ArrayList<Challenge>challenges =  iChallengeService.getAllChallenges();

        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        Timestamp challengeSentMili = new Timestamp(System.currentTimeMillis());

        Challenge challengeToAccept;

        for(Challenge chall:challenges){
            if(chall.getId()==idNo){
                challengeToAccept = chall;

                challengeToAccept.setAccepted(true);
                challengeToAccept.setOpen(true);
                challengeToAccept.setChallengeAccepted(sdf.format(challengeSentMili));

                iChallengeService.saveChallenge(challengeToAccept);
            }
        }
    }

    @RequestMapping(value = "/updateGameAccount",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void updateGameAccount(@RequestBody String id){

        /**
         * now i have the id of the challenge
         */
        int idNo = Integer.parseInt(id);

        ArrayList<Challenge>challenges =  iChallengeService.getAllChallenges();

        double stake = 0;

        Challenge thisChallenge=null;

        for(Challenge c : challenges){
            if(c.getId()==idNo){
                thisChallenge=c;
            }
        }
        stake = thisChallenge.getStake();


        User challenger = findById(String.valueOf(thisChallenge.getChallengerId()));
        GameAccount challengerGameAccount = new GameAccount();
        challengerGameAccount.setUser(challenger);
        double challengerGameBalance = stake;
        challengerGameAccount.setBalance(challengerGameBalance);
        iGameAccountService.register(challengerGameAccount);
        iUserService.register(challenger);

        User opponent = findById(String.valueOf(thisChallenge.getOpponentId()));
        GameAccount opponentGameAccount = new GameAccount();
        opponentGameAccount.setUser(opponent);
        double opponentGameBalance = stake;
        opponentGameAccount.setBalance(opponentGameBalance);
        iGameAccountService.register(opponentGameAccount);
        iUserService.register(opponent);
    }






}//end Controller
