package com.example.controllers;

import com.example.entities.Challenge;
import com.example.entities.User;
import com.example.services.IChallengeService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;



@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestContoller {

    IChallengeService iChallengeService;
    IUserService iUserService;

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

    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST, produces = "application/json")
    public void saveThisChallenge(@RequestBody String params){
        JSONObject jsonObject = new JSONObject(params);

        System.out.println(jsonObject.toString());

        String currIDString = jsonObject.getString("currUserID");
        int currID = Integer.parseInt(currIDString);

        String opponentID = jsonObject.getString("opponentID");
        int oppID = Integer.parseInt(opponentID);

        String stakeString = jsonObject.getString("stake");
        double stake = Double.parseDouble(stakeString);

        User currUserObject = findById(String.valueOf(currID));
        String currUsername =currUserObject.getUsername();

        User oppUserObject = findById(String.valueOf(oppID));
        String oppUsername = oppUserObject.getUsername();


        Challenge thisChallenge = new Challenge();

        thisChallenge.setChallengerId(currID);
        thisChallenge.setChallengerName(currUsername);

        thisChallenge.setOpponentId(oppID);
        thisChallenge.setOpponentName(oppUsername);

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

         Challenge challengeToCancel=null;

        for(Challenge ch:challenges){
            if(ch.getId()==idNo){
                challengeToCancel = ch;
                iChallengeService.deleteChallenge(challengeToCancel);
            }//end if
        }//end for
    }//end withdrawChallenge






}//end Controller
