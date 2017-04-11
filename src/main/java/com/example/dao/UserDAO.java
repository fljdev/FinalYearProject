package com.example.dao;

import com.example.entities.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;


public interface UserDAO extends JpaRepository<User, Integer>{

   User findByUsernameAndPassword(String username, String password);
   User findByUsername(String username);


}
