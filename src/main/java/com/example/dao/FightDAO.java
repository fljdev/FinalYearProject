package com.example.dao;

import com.example.entities.Fight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 07/02/2017.
 */
public interface FightDAO extends JpaRepository<Fight, Integer> {
}
