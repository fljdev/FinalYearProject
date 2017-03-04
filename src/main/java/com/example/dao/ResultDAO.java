package com.example.dao;

import com.example.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 04/03/2017.
 */
public interface ResultDAO extends JpaRepository<Trade,Integer> {
}
