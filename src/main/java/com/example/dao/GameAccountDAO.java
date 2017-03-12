package com.example.dao;

import com.example.entities.GameAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 12/03/2017.
 */
public interface GameAccountDAO extends JpaRepository<GameAccount,Integer> {
}
