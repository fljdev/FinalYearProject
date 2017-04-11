package com.example.services;

import com.example.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {

    List<User> getAllUsers();
    List<User> onlineUsers(User user);

    void register(User user);
    User deleteUser(User user);

    User findById(int id);
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);


}
