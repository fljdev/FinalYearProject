package com.example.dao;

import com.example.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeDAO extends JpaRepository<Trade,Integer> {

}
