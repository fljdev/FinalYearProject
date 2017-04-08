package com.example.controllers;

import com.example.entities.*;
import com.example.services.IBankAccountService;
import com.example.services.IGameAccountService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    IUserService iUserService;
    IBankAccountService iBankAccountService;
    IGameAccountService iGameAccountService;

    @Autowired
    public void setiGameAccountService(IGameAccountService iGameAccountService){
        this.iGameAccountService = iGameAccountService;
    }

    @Autowired
    public void setiUserService(IUserService iUserService) {

        this.iUserService = iUserService;
    }

    @Autowired
    public void setiBankAccountService(IBankAccountService iBankAccountService) {

        this.iBankAccountService = iBankAccountService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public User register(@RequestBody String jsonRegister){

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

        BankAccount account = new BankAccount();
        account.setBalance(50000);
        iBankAccountService.register(account);

        aUser.setAccount(account);
        aUser.setOnline(true);

        iUserService.register(aUser);
        return aUser;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody String jsonLogin){

        JSONObject jsonObject = new JSONObject(jsonLogin);
        String handle = jsonObject.getString("handle");
        String password = jsonObject.getString("password");

        List<User> users = iUserService.getAllUsers();

        for(User u : users){

            if(u.getUsername().equalsIgnoreCase(handle)|| u.getEmail().equalsIgnoreCase(handle)){
                if(u.getPassword().equals(password)){

                    if(u.getUsername().equalsIgnoreCase("j")){
                        u.getAccount().setBalance(u.getAccount().getBalance()+1666);
                    }
                    u.setOnline(true);
                    iUserService.register(u);
                    return u;
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    public String logout(@RequestBody User user){

        String username = user.getUsername();
        List<User> users = iUserService.getAllUsers();
        for(User u : users){
            if(u.getUsername().equalsIgnoreCase(username)){

                u.setOnline(false);
                iUserService.register(u);

                return null;
            }
        }
        return null;
    }


    @RequestMapping(value ="/allUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getAllUsers(){
        return iUserService.getAllUsers();
    }

    @RequestMapping(value = "/onlineUsers", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<User> onlineUsers(@RequestBody String username){




        List<User>users = iUserService.getAllUsers();
        List<User> onlineUsers = new ArrayList<>();

        for(User u : users ){
            if(u.isOnline() &&(!u.getUsername().equalsIgnoreCase(username))){

                onlineUsers.add(u);
            }
        }

        if(onlineUsers.size()>0){
            return onlineUsers;
        }else{
            return null ;
        }
    }


    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public User findById(@RequestBody String id){

        int i = Integer.parseInt(id);

        for(User u : iUserService.getAllUsers()){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }



    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String deleteUser(){


        User userToDelete=null;
        for(User aUser:iUserService.getAllUsers()){
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




}
