package com.example.dao;

import com.example.entities.GameAccount;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by admin on 12/03/2017.
 */
public interface GameAccountDAO extends JpaRepository<GameAccount,Integer> {
    List<GameAccount> findByUser(User user);
}
