package com.example.dao;

import com.example.entities.LiveTradeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 08/04/2017.
 */
public interface LiveTradeInfoDAO extends JpaRepository<LiveTradeInfo,Integer> {

}
