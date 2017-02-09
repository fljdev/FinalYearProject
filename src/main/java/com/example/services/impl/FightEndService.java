package com.example.services.impl;

import com.example.dao.FightEndDAO;
import com.example.entities.FightEnd;
import com.example.services.IFightEndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 09/02/2017.
 */

@Service
@Transactional
public class FightEndService implements IFightEndService{

    FightEndDAO fightEndDAO;

    @Autowired
    public void setFightEndDAO(FightEndDAO dao){
        this.fightEndDAO = dao;
    }

    @Override
    public void saveFightEnd(FightEnd fightEnd){
        fightEndDAO.save(fightEnd);
    }
}
