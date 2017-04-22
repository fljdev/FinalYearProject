package com.example.services;

import com.example.entities.Challenge;
import com.example.entities.GameAccount;
import com.example.entities.User;

/**
 * Created by admin on 12/03/2017.
 */
public interface IGameAccountService {

    void register(GameAccount gameAccount);

    GameAccount findGameAccountByUserAndChallenge(User user, Challenge challange);

}

