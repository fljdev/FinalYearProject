package com.example.services;

import com.example.entities.Challenge;
import com.example.entities.User;

import java.util.ArrayList;
import java.util.List;


public interface IChallengeService {

    void saveChallenge(Challenge challenge);

    List<Challenge> getAllChallenges();
    List<Challenge> getAllChallengesSent(User user);
    List<Challenge> getAllChallengesRecieved(User user);

    List<Challenge> getAllChallengesUserInvolvedIn(User user);
    Challenge deleteChallenge(Challenge challenge);

    Challenge findById(int id);
}
