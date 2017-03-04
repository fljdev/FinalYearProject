package com.example.dao;

import com.example.entities.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsDAO extends JpaRepository<Stats,Integer> {
}
