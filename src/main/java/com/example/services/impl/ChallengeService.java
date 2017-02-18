package com.example.services.impl;

import com.example.dao.ChallengeDAO;
import com.example.entities.Challenge;
import com.example.services.IChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 18/02/2017.
 */

@Service
@Transactional
public class ChallengeService implements IChallengeService{

    ChallengeDAO challengeDAO;

    @Autowired
    public void setChallengeDAO(ChallengeDAO dao){
        this.challengeDAO = dao;
    }

    public void saveChallenge(Challenge challenge){
        challengeDAO.save(challenge);
    }
}
