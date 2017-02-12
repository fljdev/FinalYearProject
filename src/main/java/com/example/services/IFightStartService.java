package com.example.services;

import com.example.entities.FightStart;

import java.util.ArrayList;

/**
 * Created by admin on 09/02/2017.
 */
public interface IFightStartService {

    void saveFightStart(FightStart fightStart);
    ArrayList<FightStart> getAllFights();
}
