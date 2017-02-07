package com.example.controllers;

import com.example.entities.BankAccount;
import com.example.entities.Fight;
import com.example.entities.User;
import com.example.forex.CurrencyPair;
import com.example.forex.ForexDriver;
import com.example.services.IBankAccountService;
import com.example.services.ICurrencyPairService;
import com.example.services.IFightService;
import com.example.services.IUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestController {


    IUserService iUserService;
    IBankAccountService iBankAccountService;
    IFightService iFightService;
    ICurrencyPairService iCurrencyPairService;

    @Autowired
    public void setiCurrencyPairService(ICurrencyPairService iCurrencyPairService){
        this.iCurrencyPairService = iCurrencyPairService;
    }

    @Autowired
    public void setiFightService(IFightService iFightService){
        this.iFightService = iFightService;
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

        BankAccount account = new BankAccount();
        account.setBalance(20000);
        iBankAccountService.register(account);

        aUser.setAccount(account);
        /**
         * Registering will mean you are notautomatically online.
         */
        aUser.setOnline(true);
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

    @RequestMapping(value ="/pairs", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<CurrencyPair> allPairs()throws Exception{
        ForexDriver tester = new ForexDriver();

        tester.currencyPairs.clear();

        tester.makeCurrencyPairs(tester.rawResponseList);
        tester.rawResponseList.clear();

        ArrayList<CurrencyPair>pairs= new ArrayList<CurrencyPair>();
        pairs = tester.getCurrencyPairs();

        return pairs;
    }


    @RequestMapping(value = "/onlineUsers", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ArrayList<User> onlineUsers(@RequestBody String username){

        System.out.println("\n\n\n --------"+username);

        ArrayList<User>users = new ArrayList((ArrayList<User>) iUserService.getAllUsers());
        ArrayList<User> onlineUsers = new ArrayList<>();

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
        ArrayList<User> users = iUserService.getAllUsers();

        for(User u : users){
            if(u.getId()== i ){
                return u;
            }
        }
        return null;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody String jsonLogin){

        JSONObject jsonObject = new JSONObject(jsonLogin);
        String handle = jsonObject.getString("handle");
        String password = jsonObject.getString("password");

        ArrayList<User> users = iUserService.getAllUsers();

        for(User u : users){

            if(u.getUsername().equalsIgnoreCase(handle)|| u.getEmail().equalsIgnoreCase(handle)){
                if(u.getPassword().equals(password)){

                    if(u.getUsername().equalsIgnoreCase("j")){
                        u.getAccount().setBalance(u.getAccount().getBalance()+1666);
                    }


                    u.setOnline(true);
                    iUserService.register(u);
                    return u;
                }//end if
            }//end if
        }//end for
        return null;
    }

    @RequestMapping(value = "/getFightGameObject", method = RequestMethod.POST, produces = "application/json")
    public Fight fight(@RequestBody String jsonLogin)throws Exception{

        JSONObject jsonObject = new JSONObject(jsonLogin);
//        System.out.println(jsonObject);

        String cPair = jsonObject.getString("cPair");
        String cDir = jsonObject.getString("currUserDirection");
        String cStake = jsonObject.getString("currUserStake");
        String cLev = jsonObject.getString("currUserLeverage");

        String oPair = jsonObject.getString("oPair");
        String oDir = jsonObject.getString("askedUserDirection");
        String oStake = jsonObject.getString("askedUserStake");
        String oLev = jsonObject.getString("askedUserLeverage");

        ArrayList<CurrencyPair>pairs = new ArrayList<>();
        pairs=allPairs();
        CurrencyPair challengerPair = new CurrencyPair();
        CurrencyPair opponentPair = new CurrencyPair();
        for(CurrencyPair cp : pairs){
            if(cp.getSymbols().equalsIgnoreCase(cPair)){
                challengerPair=cp;
                iCurrencyPairService.saveCurrencyPair(challengerPair);
            }
            if(cp.getSymbols().equalsIgnoreCase(oPair)){
                opponentPair=cp;
                iCurrencyPairService.saveCurrencyPair(opponentPair);
            }
        }
        ArrayList<CurrencyPair>temp = new ArrayList<>();
        temp.add(challengerPair);
        temp.add(opponentPair);
        Set<CurrencyPair> set = new HashSet<CurrencyPair>(temp);


        Fight aFight = new Fight();

        aFight.setChallengerID(11);
        aFight.setPairs(set);
        aFight.setChallengerDirection(cDir);
        aFight.setChallengerStake(Double.parseDouble(cStake));
        aFight.setChallengerLeverage(Double.parseDouble(cLev));
        aFight.setOpponentID(14);
        aFight.setOpponentDirection(oDir);
        aFight.setOpponentStake(Double.parseDouble(oStake));
        aFight.setOpponentLeverage(Double.parseDouble(oLev));

        System.out.println(aFight.toString());
        iFightService.saveFight(aFight);

//        System.out.println(cPair);
//        System.out.println(cDir);
//        System.out.println(cStake);
//        System.out.println(cLev);
//        System.out.println(cPair);
//        System.out.println(cDir);
//        System.out.println(cStake);
//        System.out.println(cLev);

        return null;
    }




//        ArrayList<User> users = iUserService.getAllUsers();
//
//        for(User u : users){
//
//            if(u.getUsername().equalsIgnoreCase(handle)|| u.getEmail().equalsIgnoreCase(handle)){
//                if(u.getPassword().equals(password)){
//
//                    if(u.getUsername().equalsIgnoreCase("j")){
//                        u.getAccount().setBalance(u.getAccount().getBalance()+1666);
//                    }
//
//
//                    u.setOnline(true);
//                    iUserService.register(u);
//                    return u;
//                }
//            }
//
////            if((u.getUsername().equalsIgnoreCase(handle)|| u.getEmail().equalsIgnoreCase(handle) &&  u.getPassword().equals(password))){
////                u.setOnline(true);
////                iUserService.register(u);
////                return u;
////


    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    public String logout(@RequestBody User user){

        String username = user.getUsername();
        ArrayList<User> users = iUserService.getAllUsers();
        for(User u : users){
            if(u.getUsername().equalsIgnoreCase(username)){

                u.setOnline(false);
//                iUserService.deleteUser(u);
                iUserService.register(u);
                System.out.println(username + " has been logged out!");

                return null;
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
