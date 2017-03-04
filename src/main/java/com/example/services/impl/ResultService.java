package com.example.services.impl;

import com.example.dao.ResultDAO;
import com.example.services.IResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class ResultService implements IResultService{

    ResultDAO resultDao;

    @Autowired
    public void setDao(ResultDAO dao){
        this.resultDao = dao;
    }


}
