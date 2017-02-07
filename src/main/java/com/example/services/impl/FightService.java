package com.example.services.impl;

import com.example.dao.FightDAO;
import com.example.entities.Fight;
import com.example.services.IFight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 07/02/2017.
 */

@Service
@Transactional
public class FightService implements IFight {

    FightDAO fightDAO;

    @Autowired
    public void setFightDAO(FightDAO dao){

        this.fightDAO = dao;
    }

    @Override
    public void saveFight(Fight fight){

    }
}
