package com.example.dao;

import com.example.entities.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 26/10/2016.
 */
public interface UserDAO extends JpaRepository<User, Long>{

    User findByUsername(String name);

}
