package com.example.services.impl;



import com.example.dao.LiveTradeInfoDAO;
import com.example.entities.LiveTradeInfo;
import com.example.services.ILiveTradeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 08/04/2017.
 */
@Service
@Transactional
public class LiveTradeInfoService implements ILiveTradeInfo {

    LiveTradeInfoDAO liveTradeInfoDAO;

    @Autowired
    public void setLiveTradeInfoDAO(LiveTradeInfoDAO dao){
        this.liveTradeInfoDAO = dao;
    }

    @Override
    public List<LiveTradeInfo> getAllLiveTradeInfo() {
        return liveTradeInfoDAO.findAll();
    }

    @Override
    public void saveLiveTradeInfo(LiveTradeInfo liveTradeInfo) {
        liveTradeInfoDAO.save(liveTradeInfo);

    }

    @Override
    public LiveTradeInfo findLiveTradeInfoObjectByTradeID(int tradeID) {
        return liveTradeInfoDAO.findLiveTradeInfoObjectByTradeID(tradeID);
    }
}
