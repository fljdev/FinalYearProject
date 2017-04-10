package com.example.services.impl;

import com.example.dao.UserDAO;
import com.example.entities.User;
import com.example.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class UserService implements IUserService{

    UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO dao){

        this.userDAO = dao;
    }


    @Override
    public List<User> getAllUsers() {

        ArrayList<User>usersFound = new ArrayList<User>();
        for(User aUser: userDAO.findAll()){
            usersFound.add(aUser);
        }
        return usersFound;
    }

    @Override
    public List<User> onlineUsers() {

        ArrayList<User>usersOnline = new ArrayList<User>();
        for(User aUser: userDAO.findAll()){

            if(aUser.isOnline()){


                usersOnline.add(aUser);
            }
        }
        return usersOnline;
    }

    @Override
    public void register(User user) {

        userDAO.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    @Override
    public User findById(int id) {
        return userDAO.findOne(id);
    }
}
