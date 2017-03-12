package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public GameAccount(double balance) {
        this.balance = balance;
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

    @Override
    public String toString() {
        return "GameAccount{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
