package com.example.dao;

import com.example.entities.BankAccount;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 30/01/2017.
 */
public interface BankAccountDAO extends JpaRepository<BankAccount, Integer> {

}
