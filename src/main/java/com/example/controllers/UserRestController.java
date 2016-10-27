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






    @RequestMapping(value ="/allUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<User> getAllUsers(){

        ArrayList<User>users = new ArrayList<User>();

        users=(ArrayList<User>) iUserService.getAllUsers();

        return users;
    }


    @RequestMapping(value = "/createUsers", method = RequestMethod.POST)
    @ResponseBody
    public String createUser(String firstName,String lastName, String username, String email, String password){

        User aUser = new User();
        aUser.setFirstName(firstName);
        aUser.setLastName(lastName);
        aUser.setUsername(username);
        aUser.setEmail(email);
        aUser.setPassword(password);

        iUserService.createUser(aUser);

        return "Well done, "+aUser.getUsername()+" has been added to the database:)";
    }

    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String deleteUser(){

        ArrayList<User>users = iUserService.getAllUsers();

        User userToDelete=null;
        for(User aUser:users){
            if(aUser.getUsername().equalsIgnoreCase("Joni")){
                userToDelete=aUser;
            }
        }

        if(userToDelete!=null){
            iUserService.deleteUser(userToDelete);

            return userToDelete.getUsername()+ " has been deleted";
        }else{
            return "Could not find user in db";
        }
    }




}//end class
