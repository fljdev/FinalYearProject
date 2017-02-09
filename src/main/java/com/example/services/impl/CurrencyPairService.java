package com.example.services.impl;

import com.example.dao.CurrencyPairDAO;
import com.example.entities.CurrencyPair;
import com.example.services.ICurrencyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 07/02/2017.
 */

@Service
@Transactional
public class CurrencyPairService implements ICurrencyPairService {
    CurrencyPairDAO currencyPairDAO;

    @Autowired
    public void setCurrencyPairDAO(CurrencyPairDAO currencyPairDAO){

        this.currencyPairDAO = currencyPairDAO;
    }

    @Override
    public void saveCurrencyPair(CurrencyPair cp ){
        currencyPairDAO.save(cp);

    }



}
