package com.example.controllers;

import com.example.entities.LiveTradeInfo;
import com.example.services.ILiveTradeInfoService;
import com.example.services.ITradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by admin on 19/04/2017.
 */
@RestController
@RequestMapping("/api/liveTradeInfoController")
public class LiveTradeInfoController {
    ITradeService iTradeService;
    ILiveTradeInfoService iLiveTradeInfoService;

    @Autowired
    public void setiTradeService(ITradeService iTradeService){
        this.iTradeService = iTradeService;
    }

    @Autowired
    public void setiLiveTradeInfoService(ILiveTradeInfoService service){
        this.iLiveTradeInfoService = service;
    }

    @RequestMapping(value = "/findLiveTradeInfoObjectByTradeID", method = RequestMethod.POST, produces = "application/json")
    public List<LiveTradeInfo> findById(@RequestBody int id){
        return iLiveTradeInfoService.findLiveTradeInfoObjectByTradeID(id);
    }

}
