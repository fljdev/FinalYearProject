package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by admin on 04/03/2017.
 */

@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User winner;

    @ManyToOne
    private User loser;

    @OneToOne
    private Challenge challenge;

    private double prize;


    public Result() {
    }

    public Result(User winner, User loser, Challenge challenge, double prize) {
        this.winner = winner;
        this.loser = loser;
        this.challenge = challenge;
        this.prize = prize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", winner=" + winner +
                ", loser=" + loser +
                ", challenge=" + challenge +
                ", prize=" + prize +
                '}';
    }
}
