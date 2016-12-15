package com.example.controllers;

import com.example.entities.User;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by admin on 26/10/2016.
 */

@RestController
@RequestMapping("/api")
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


    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseBody
    public String createUser(String firstName,String lastName, String username, String email, String password){

        User aUser = new User();
        aUser.setFirstName(firstName);
        aUser.setLastName(lastName);
        aUser.setUsername(username);
        aUser.setEmail(email);
        aUser.setPassword(password);

        /**
         * Registering will mean you are notautomatically online.
         */
//        aUser.setOnline(true);

        iUserService.createUser(aUser);

        return "Well done, "+aUser.getUsername()+" has been added to the database:)";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody String jsonLogin){

        System.out.println(jsonLogin);
        JSONObject jsonObject = new JSONObject(jsonLogin);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");


        boolean correctLoginDetails = false;
        ArrayList<User> users = iUserService.getAllUsers();
        for(User u : users){
            if(u.getUsername().equalsIgnoreCase(username)&& u.getPassword().equals(password)){
                correctLoginDetails=true;


                u.setOnline(true);
                System.out.println(u.toString());
                return u;
            }
        }

        return null;
    }

//    @RequestMapping(value = "/onlineUsers", method = RequestMethod.POST)
//    @ResponseBody
//    public ArrayList<User> onlineUsers(){
//
//        return null;
//
//    }

    @RequestMapping("/onlineUsers")
    void handleFoo(HttpServletResponse response) {
        try {
            response.sendRedirect("ff.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
