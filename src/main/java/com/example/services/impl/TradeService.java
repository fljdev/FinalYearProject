package com.example.services.impl;

import com.example.dao.TradeDAO;
import com.example.entities.Trade;
import com.example.services.ITradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by admin on 26/02/2017.
 */

@Service
@Transactional
public class TradeService implements ITradeService {
    TradeDAO tradeDAO;

    @Autowired
    public void setTradeDAO(TradeDAO dao){
        this.tradeDAO = dao;
    }

    @Override
    public void saveTrade(Trade trade) {
        tradeDAO.save(trade);
    }

    @Override
    public ArrayList<Trade> getAllTrades() {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        for(Trade t : tradeDAO.findAll()){
            trades.add(t);
        }
        return trades;
    }
}