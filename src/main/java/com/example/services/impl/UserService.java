package com.example.services.impl;

import com.example.dao.UserDAO;
import com.example.entities.User;
import com.example.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2016.
 */

@Service
@Transactional
public class UserService implements IUserService{

    UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO dao){

        this.userDAO = dao;
    }


    @Override
    public ArrayList<User> getAllUsers() {

        ArrayList<User>u = new ArrayList<User>();
        for(User aUser: userDAO.findAll()){
            u.add(aUser);
        }

        return u;
    }

    @Override
    public void createUser(User user) {

        userDAO.save(user);


    }

    @Override
    public void deleteUser(User user) {
        userDAO.delete(user);
    }


}
