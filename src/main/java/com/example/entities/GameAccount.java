package com.example.entities;

import javax.persistence.*;

/**
 * Created by admin on 12/03/2017.
 */

@Entity
public class GameAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double balance;

    public GameAccount() {
    }

    @ManyToOne
    private User user;


    public GameAccount(double balance, User user) {
        this.balance = balance;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
