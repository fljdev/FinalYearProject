package com.example.services;

import com.example.entities.User;

import java.util.ArrayList;

public interface IUserService {

    ArrayList<User> getAllUsers();
    ArrayList<User> onlineUsers();

    void register(User user);
    void deleteUser(User user);

}
