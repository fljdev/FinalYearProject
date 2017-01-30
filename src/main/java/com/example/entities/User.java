package com.example.entities;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Generated;
import javax.persistence.*;

/**
 * Created by admin on 26/10/2016.
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private BankAccount account;


    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private boolean online;


    //private CurrencyPair currencyPair; Object not created yet
    //private Rank rank; Object not created yet


    public User() {
    }


    public User(String firstName, String lastName, String username, String email, String password,BankAccount account, boolean online) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.account = account;
        this.online = online;
    }

    public int getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", account=" + account +
                ", online=" + online +
                '}';
    }
}
