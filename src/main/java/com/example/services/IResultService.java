package com.example.services;

import com.example.entities.Challenge;
import com.example.entities.Result;
import com.example.entities.User;

import java.util.List;

/**
 * Created by admin on 04/03/2017.
 */
public interface IResultService {

    void saveResult(Result result);
    List<Result> getAllResults();
    Result findById(int id);
    List<Result> findByUser(User user);
    Result findByChallenge(Challenge challenge);

}
