package com.example.services;

import com.example.entities.Challenge;

import java.util.ArrayList;


public interface IChallengeService {

    void saveChallenge(Challenge challenge);

    ArrayList<Challenge> getAllChallenges();
    void deleteChallenge(Challenge challenge);
}
