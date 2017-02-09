//package com.example.services.impl;
//
//import com.example.dao.CurrencyPairSetDAO;
//import com.example.entities.CurrencyPairSet;
//import com.example.services.ICurrencyPairSetService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Created by admin on 09/02/2017.
// */
//
//@Service
//@Transactional
//public class CurrencyPairSetService implements ICurrencyPairSetService {
//
//    CurrencyPairSetDAO currencyPairSetDAO;
//
//    @Autowired
//    public void setCurrencyPairSetDAO(CurrencyPairSetDAO dao){
//        this.currencyPairSetDAO = dao;
//    }
//
//    @Override
//    public void saveCurrencyPairSet(CurrencyPairSet currencyPairSet) {
//        currencyPairSetDAO.save(currencyPairSet);
//    }
//}
