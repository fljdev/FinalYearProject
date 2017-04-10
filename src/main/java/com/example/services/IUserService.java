package com.example.services;

import com.example.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {

    List<User> getAllUsers();
    List<User> onlineUsers();

    void register(User user);
    void deleteUser(User user);

    User findById(int id);

}
