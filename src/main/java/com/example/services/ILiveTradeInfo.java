package com.example.services;



import com.example.dao.LiveTradeInfoDAO;
import com.example.entities.LiveTradeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 08/04/2017.
 */
public interface ILiveTradeInfo {

    List<LiveTradeInfo> getAllLiveTradeInfo();

    void saveLiveTradeInfo(LiveTradeInfo liveTradeInfo);
}
