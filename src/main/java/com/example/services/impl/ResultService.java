package com.example.services.impl;

import com.example.dao.ResultDAO;
import com.example.entities.Challenge;
import com.example.entities.Result;
import com.example.entities.User;
import com.example.services.IResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ResultService implements IResultService{

    ResultDAO resultDao;

    @Autowired
    public void setDao(ResultDAO dao){
        this.resultDao = dao;
    }

    @Override
    public void saveResult(Result result) {
        resultDao.save(result);
    }

    @Override
    public List<Result> getAllResults() {
        return resultDao.findAll();
    }

    @Override
    public Result findById(int id) {
        return resultDao.findOne(id);
    }

    @Override
    public List<Result> findByUser(User user) {
        List<Result>userResults = new ArrayList<>();
        for(Result result : resultDao.findAll()){
            if(result.getWinner()==user || result.getLoser()==user){
                userResults.add(result);
            }

        }
        return userResults;
    }

    @Override
    public Result findByChallenge(Challenge challenge) {
        for(Result r : resultDao.findAll()){
            if(r.getChallenge()==challenge){
                return r;
            }
        }
        return null;
    }

}
