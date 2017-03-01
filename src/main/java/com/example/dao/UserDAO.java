package com.example.dao;

import com.example.entities.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;


public interface UserDAO extends JpaRepository<User, Integer>{

}
