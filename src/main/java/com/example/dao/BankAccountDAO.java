package com.example.dao;

import com.example.entities.BankAccount;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BankAccountDAO extends JpaRepository<BankAccount, Integer> {

}
