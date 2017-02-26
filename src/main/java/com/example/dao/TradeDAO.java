package com.example.dao;

import com.example.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 26/02/2017.
 */
public interface TradeDAO extends JpaRepository<Trade,Integer> {

}
