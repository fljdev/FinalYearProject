package com.example.services;

import com.example.entities.Challenge;

import java.util.ArrayList;
import java.util.List;


public interface IChallengeService {

    void saveChallenge(Challenge challenge);

    List<Challenge> getAllChallenges();
    void deleteChallenge(Challenge challenge);
}
