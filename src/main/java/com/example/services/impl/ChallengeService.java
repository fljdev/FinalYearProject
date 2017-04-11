package com.example.services.impl;

import com.example.dao.ChallengeDAO;
import com.example.entities.Challenge;
import com.example.entities.User;
import com.example.services.IChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ChallengeService implements IChallengeService{

    ChallengeDAO challengeDAO;
    @Autowired
    public void setChallengeDAO(ChallengeDAO dao){
        this.challengeDAO = dao;
    }



    @Override
    public void saveChallenge(Challenge challenge){
        challengeDAO.save(challenge);
    }


    @Override
    public List<Challenge> getAllChallenges() {
        return challengeDAO.findAll();
    }


    @Override
    public Challenge deleteChallenge(Challenge challenge) {

        challengeDAO.delete(challenge);
        return challenge;
    }


    @Override
    public Challenge findById(int id) {
        return challengeDAO.findOne(id);
    }

    @Override
    public List<Challenge> getAllChallengesSent(User user) {
        List<Challenge> sent =new ArrayList<>();
        for(Challenge c : challengeDAO.findAll()){
            if(c.getChallengerId()==user.getId()){
                sent.add(c);
            }
        }
        return sent;
    }

    @Override
    public List<Challenge> getAllChallengesRecieved(User user) {
        List<Challenge>recieved=new ArrayList<>();
        for(Challenge c : challengeDAO.findAll()){
            if(c.getOpponentId()==user.getId()){
                recieved.add(c);
            }
        }
        return recieved;
    }
}
