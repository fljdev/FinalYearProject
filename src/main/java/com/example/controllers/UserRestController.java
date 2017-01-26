package com.example.controllers;

import com.example.entities.User;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class UserRestController {


    IUserService iUserService;

    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public User register(@RequestBody String jsonRegister){
        System.out.println("got in here");

        JSONObject jsonObject = new JSONObject(jsonRegister);
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");

        if(!password.equalsIgnoreCase(confirmPassword)){
            return null;
        }

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

        iUserService.register(aUser);

        return aUser;
    }


    @RequestMapping(value ="/allUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<User> getAllUsers(){
        ArrayList<User>users = new ArrayList();
        users=(ArrayList<User>) iUserService.getAllUsers();
        return users;
    }


    @RequestMapping(value = "/onlineUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<User> onlineUsers(){

        ArrayList<User>users = new ArrayList();
        users=(ArrayList<User>) iUserService.getAllUsers();

//        return users;

        ArrayList<User> onlineUsers = new ArrayList<>();

        for(User u : users ){
            if(u.isOnline()){

                System.out.println("Online Status "+u.getUsername()+" "+u.isOnline());
                onlineUsers.add(u);
            }
        }

        System.out.println("\n\n\n-----Online Users----- \n\n\n");
        onlineUsers.size();

        if(onlineUsers.size()>0){
            return onlineUsers;
        }else{
            return null ;
        }

    }



    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody String jsonLogin){

        System.out.println(jsonLogin);
        JSONObject jsonObject = new JSONObject(jsonLogin);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");


        ArrayList<User> users = iUserService.getAllUsers();
        for(User u : users){
            if(u.getUsername().equalsIgnoreCase(username)&& u.getPassword().equals(password)){


                u.setOnline(true);
                iUserService.register(u);
                System.out.println("status = " + u.isOnline());
                System.out.println(u.toString());
                return u;
            }
        }

        return null;
    }





//    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET, produces = "application/json")
//    @ResponseBody
//    public String deleteUser(){
//
//        ArrayList<User>users = iUserService.getAllUsers();
//
//        User userToDelete=null;
//        for(User aUser:users){
//            if(aUser.getUsername().equalsIgnoreCase("Joni")){
//                userToDelete=aUser;
//            }
//        }
//
//        if(userToDelete!=null){
//            iUserService.deleteUser(userToDelete);
//
//            return userToDelete.getUsername()+ " has been deleted";
//        }else{
//            return "Could not find user in db";
//        }
//    }




}//end class
