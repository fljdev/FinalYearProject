package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by admin on 04/03/2017.
 */

@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int winnerID;
    private int loserID;
    private double amount;

    public Result() {
    }

    public Result(int winnerID, int loserID, double amount) {
        this.winnerID = winnerID;
        this.loserID = loserID;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }


    public int getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }

    public int getLoserID() {
        return loserID;
    }

    public void setLoserID(int loserID) {
        this.loserID = loserID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", winnerID=" + winnerID +
                ", loserID=" + loserID +
                ", amount=" + amount +
                '}';
    }
}
