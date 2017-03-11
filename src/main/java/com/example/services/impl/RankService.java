package com.example.services.impl;

import com.example.dao.RankDAO;
import com.example.entities.Rank;
import com.example.services.IRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by admin on 02/03/2017.
 */
@Service
@Transactional
public class RankService implements IRankService {

    RankDAO rankDAO;

    @Autowired
    public void setRankDAO(RankDAO dao){
        this.rankDAO = dao;
    }


    public void saveRank(Rank rank){

        rankDAO.save(rank);
    }

    @Override
    public ArrayList<Rank> getALlRanks() {
        ArrayList<Rank>ranks = new ArrayList<>();
        for(Rank r : rankDAO.findAll()){
            ranks.add(r);
        }
        return ranks;
    }

    @Override
    public void deleteRank(Rank rank) {
        rankDAO.delete(rank);
    }






}
