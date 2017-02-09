package com.example.dao;

import com.example.entities.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 07/02/2017.
 */
public interface CurrencyPairDAO extends JpaRepository<CurrencyPair, Integer> {
}
