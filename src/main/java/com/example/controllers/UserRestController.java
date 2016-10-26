package com.example.controllers;

import com.example.entities.User;
import com.example.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2016.
 */

@RestController
public class UserRestController {


    IUserService iUserService;

    @Autowired
    public void setiUserService(IUserService iUserService) {
        this.iUserService = iUserService;
    }





    @RequestMapping("/")
    public ArrayList<User> getAllUsers(){

        ArrayList<User>users = new ArrayList<User>();

        users=(ArrayList<User>) iUserService.getAllUsers();

        return users;
    }


    @RequestMapping(value = "/createUsers", method = RequestMethod.GET, produces = "application/json")    @ResponseBody
    public String createUser(){

        User aUser = new User("john");

        iUserService.createUser(aUser);

        return "done";
    }


}
