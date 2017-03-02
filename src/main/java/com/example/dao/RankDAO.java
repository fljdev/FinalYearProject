package com.example.dao;

import com.example.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 02/03/2017.
 */
public interface RankDAO extends JpaRepository<Rank,Integer> {
}
