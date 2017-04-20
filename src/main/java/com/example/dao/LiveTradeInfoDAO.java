package com.example.dao;

import com.example.entities.LiveTradeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by admin on 08/04/2017.
 */
public interface LiveTradeInfoDAO extends JpaRepository<LiveTradeInfo,Integer> {

    List<LiveTradeInfo> findLiveTradeInfoObjectByTradeID(int tradeID);

}
