package com.example.services.impl;

import com.example.dao.GameAccountDAO;
import com.example.entities.GameAccount;
import com.example.services.IGameAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
