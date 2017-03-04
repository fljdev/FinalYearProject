package com.example.controllers;


import com.example.services.IStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    IStatsService iStatsService;

    @Autowired
    public void setiStatsService(IStatsService iStatsService){
        this.iStatsService = iStatsService;
    }
}
