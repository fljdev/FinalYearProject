package com.example.dao;

import com.example.entities.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2016.
 */
public interface UserDAO extends JpaRepository<User, Integer>{

//    User findByUsername(String name);

//    ArrayList<User> getAllUsers();

}
