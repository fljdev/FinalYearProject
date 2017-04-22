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

    @ManyToOne
    private User user;

    @OneToOne
    private Challenge challenge;

    public GameAccount() {
    }

    public GameAccount(double balance, User user, Challenge challenge) {
        this.balance = balance;
        this.user = user;
        this.challenge = challenge;
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

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
