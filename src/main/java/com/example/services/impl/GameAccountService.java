package com.example.services.impl;

import com.example.dao.GameAccountDAO;
import com.example.entities.Challenge;
import com.example.entities.GameAccount;
import com.example.entities.User;
import com.example.services.IGameAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/03/2017.
 */


@Service
@Transactional
public class GameAccountService implements IGameAccountService {

    GameAccountDAO gameAccountDAO;

    @Autowired
    public void setGameAccountDAO(GameAccountDAO dao){
        this.gameAccountDAO = dao;
    }

    @Override
    public void register(GameAccount gameAccount) {
        gameAccountDAO.save(gameAccount);
    }

    @Override
    public GameAccount findGameAccountByUserAndChallenge(User user, Challenge challange) {
        for(GameAccount ga: gameAccountDAO.findAll()){
            if(ga.getUser()==user && ga.getChallenge()==challange){
                return  ga;
            }
        }
        return null;
    }


    @Override
    public List<GameAccount> findByUser(User user) {

        return gameAccountDAO.findByUser(user);
    }
}
