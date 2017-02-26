package com.example.services;

import com.example.entities.Trade;

import java.util.ArrayList;

/**
 * Created by admin on 26/02/2017.
 */
public interface ITradeService {

    void saveTrade(Trade trade);

    ArrayList<Trade> getAllTrades();

}
