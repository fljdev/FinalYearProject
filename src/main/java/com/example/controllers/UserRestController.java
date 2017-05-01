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
        boolean unique = true;
        for(User u : iUserService.getAllUsers()){
            if(u.getEmail().equalsIgnoreCase(email) || u.getUsername().equalsIgnoreCase(username)){
                unique=false;
            }
        }
        if(!unique){
            return null;
        }
        User aUser = new User();
        aUser.setFirstName(firstName);
        aUser.setLastName(lastName);
        aUser.setUsername(username);
        aUser.setEmail(email);
        aUser.setPassword(password);

        BankAccount account = new BankAccount();
        account.setBalance(100000);
        iBankAccountService.register(account);

        aUser.setAccount(account);
        aUser.setOnline(true);





        iUserService.register(aUser);
        return aUser;
    }



    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody String jsonLogin){
        System.out.println("log with "+jsonLogin);
        JSONObject jsonObject = new JSONObject(jsonLogin);
        String handle = jsonObject.getString("handle");
        String password = jsonObject.getString("password");
        User u = iUserService.findByUsernameAndPassword(handle,password);
        u.setOnline(true);
        iUserService.register(u);
        return u;
    }



    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    public User logout(@RequestBody User user){
        User userToLogout = iUserService.findByUsername(user.getUsername());
        userToLogout.setOnline(false);
        iUserService.register(userToLogout);
        return userToLogout;
    }



    @RequestMapping(value ="/allUsers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getAllUsers(){
        return iUserService.getAllUsers();
    }



    @RequestMapping(value = "/onlineUsers", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<User> onlineUsers(@RequestBody User user){
        return iUserService.onlineUsers(user);
    }



    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public User findById(@RequestBody String id){
        return iUserService.findById(Integer.parseInt(id));
    }


    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public User deleteUser(User user){
        return iUserService.deleteUser(user);
    }

}
