package com.example.services;

import com.example.entities.BankAccount;
import com.example.entities.User;

import java.util.List;


public interface IBankAccountService {

    void register(BankAccount account);

    List<BankAccount> findAllAccounts();
}
