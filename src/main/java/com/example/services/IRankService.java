package com.example.services;

import com.example.entities.Rank;

import java.util.ArrayList;

/**
 * Created by admin on 02/03/2017.
 */
public interface IRankService {

    void saveRank(Rank rank);
    ArrayList<Rank> getALlRanks();


}
