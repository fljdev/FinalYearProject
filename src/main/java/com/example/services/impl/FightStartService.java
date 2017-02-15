package com.example.services.impl;

import com.example.dao.FightStartDAO;
import com.example.entities.FightStart;
import com.example.services.IFightStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by admin on 09/02/2017.
 */

@Service
@Transactional
public class FightStartService implements IFightStartService {

    FightStartDAO fightStartDAO;

    @Autowired
    public void setFightStartDAO(FightStartDAO dao){
        this.fightStartDAO = dao;
    }

    @Override
    public void saveFightStart(FightStart fightStart) {
        fightStartDAO.save(fightStart);
    }



    @Override
    public ArrayList<FightStart> getAllFights() {
        ArrayList<FightStart> allFights = new ArrayList<FightStart>();

        for(FightStart fs : fightStartDAO.findAll()){
            allFights.add(fs);
            System.out.println(fs.toString());
        }

        return allFights;
    }


}
