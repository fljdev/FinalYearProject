package com.example.services;

import com.example.entities.Trade;

import java.util.ArrayList;


public interface ITradeService {

    void saveTrade(Trade trade);

    ArrayList<Trade> getAllTrades();

}
