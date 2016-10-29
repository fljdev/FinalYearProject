package com.example.services;

import com.example.entities.User;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2016.
 */
public interface IUserService {

    ArrayList<User> getAllUsers();
    ArrayList<User> onlineUsers();

    void createUser(User user);
    void deleteUser(User user);

}
