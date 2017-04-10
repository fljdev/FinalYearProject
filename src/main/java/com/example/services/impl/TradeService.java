package com.example.services.impl;

import com.example.dao.TradeDAO;
import com.example.entities.Trade;
import com.example.entities.User;
import com.example.services.ITradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class TradeService implements ITradeService {
    TradeDAO tradeDAO;

    @Autowired
    public void setTradeDAO(TradeDAO dao){
        this.tradeDAO = dao;
    }



    @Override
    public List<Trade> getAllTrades() {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        for(Trade t : tradeDAO.findAll()){
            trades.add(t);
        }
        return trades;
    }

    @Override
    public Trade findTradeById(int id) {
        return tradeDAO.findTradeById(id);
    }

    @Override
    public List<Trade> findByUser(User user) {
        return tradeDAO.findByUser(user);
    }

    @Override
    public void saveTrade(Trade trade) {
        trade.setOpen(true);
        tradeDAO.save(trade);
    }

    @Override
    public void updateAndSaveTrade(Trade trade) {
        trade.setOpen(false);
        tradeDAO.save(trade);


    }
}
