package com.example.services;

import com.example.entities.Challenge;

import java.util.ArrayList;

/**
 * Created by admin on 18/02/2017.
 */
public interface IChallengeService {

    void saveChallenge(Challenge challenge);

    ArrayList<Challenge> getAllChallenges();
}
