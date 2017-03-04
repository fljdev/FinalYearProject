package com.example.services.impl;

import com.example.dao.StatsDAO;
import com.example.services.IStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatsService implements IStatsService {

    StatsDAO statsDAO;

    @Autowired
    public void setStatsDAO(StatsDAO statsDAO){
        this.statsDAO = statsDAO;
    }

}
