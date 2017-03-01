package com.example.dao;

import com.example.entities.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyPairDAO extends JpaRepository<CurrencyPair, Integer> {
}
