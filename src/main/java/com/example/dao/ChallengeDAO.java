package com.example.dao;

import com.example.entities.Challenge;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeDAO extends JpaRepository<Challenge,Integer> {

}
