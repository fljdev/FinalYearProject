package com.example.services;

import com.example.entities.Trade;

import java.util.ArrayList;
import java.util.List;


public interface ITradeService {

    void saveTrade(Trade trade);
    List<Trade> getAllTrades();

    Trade findTradeById(int id);

}
