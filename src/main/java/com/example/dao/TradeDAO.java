package com.example.dao;

import com.example.entities.Trade;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TradeDAO extends JpaRepository<Trade,Integer> {
    Trade findTradeById(int id);

    List<Trade> findByUser(User user);

}
