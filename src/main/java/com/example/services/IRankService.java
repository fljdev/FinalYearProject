package com.example.services;

import com.example.entities.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 02/03/2017.
 */
public interface IRankService {

    void saveRank(Rank rank);
    List<Rank> getALlRanks();

    void deleteRank(Rank rank);


}
