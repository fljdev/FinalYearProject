package com.example.services.impl;

import com.example.dao.BankAccountDAO;
import com.example.entities.BankAccount;
import com.example.entities.User;
import com.example.services.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class BankAccountService implements IBankAccountService {

    BankAccountDAO bankAccountDAO;

    @Autowired
    public void setBankAccountDAO(BankAccountDAO dao){

        this.bankAccountDAO = dao;
    }

    @Override
    public void register(BankAccount account) {

        bankAccountDAO.save(account);
    }

    @Override
    public List<BankAccount> findAllAccounts() {
        return bankAccountDAO.findAll();
    }
}
