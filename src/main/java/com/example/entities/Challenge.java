package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    User challenger;
    @ManyToOne
    User opponent;


    private int duration;

    @Column(name = "$")
    private double stake;

    @Column(name = "challProfit")
    private double challengerProfit;

    @Column(name = "oppProfit")
    private double opponentProfit;

    private boolean accepted;
    private boolean declined;
    private boolean withdrawen;
    private boolean open;
    private boolean completed;

    private int challengeRequestValidTime;



    public Challenge() {
    }

    public Challenge(User challenger, User opponent, int duration, double stake, double challengerProfit,
                     double opponentProfit, boolean accepted, boolean declined, boolean withdrawen,
                     boolean open, boolean completed, int challengeRequestValidTime) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.duration = duration;
        this.stake = stake;
        this.challengerProfit = challengerProfit;
        this.opponentProfit = opponentProfit;
        this.accepted = accepted;
        this.declined = declined;
        this.withdrawen = withdrawen;
        this.open = open;
        this.completed = completed;
        this.challengeRequestValidTime = challengeRequestValidTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getChallenger() {
        return challenger;
    }

    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public double getChallengerProfit() {
        return challengerProfit;
    }

    public void setChallengerProfit(double challengerProfit) {
        this.challengerProfit = challengerProfit;
    }

    public double getOpponentProfit() {
        return opponentProfit;
    }

    public void setOpponentProfit(double opponentProfit) {
        this.opponentProfit = opponentProfit;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }

    public boolean isWithdrawen() {
        return withdrawen;
    }

    public void setWithdrawen(boolean withdrawen) {
        this.withdrawen = withdrawen;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getChallengeRequestValidTime() {
        return challengeRequestValidTime;
    }

    public void setChallengeRequestValidTime(int challengeRequestValidTime) {
        this.challengeRequestValidTime = challengeRequestValidTime;
    }
}
