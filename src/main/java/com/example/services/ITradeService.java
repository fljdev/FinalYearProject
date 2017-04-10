package com.example.services;

import com.example.entities.Trade;
import com.example.entities.User;

import java.util.ArrayList;
import java.util.List;


public interface ITradeService {

    void saveTrade(Trade trade);
    void updateAndSaveTrade(Trade trade);
    List<Trade> getAllTrades();

    Trade findTradeById(int id);

    List<Trade> findByUser(User user);

}
