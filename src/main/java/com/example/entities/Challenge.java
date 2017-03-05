package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int challengerId;
    private String challengerName;

    private int opponentId;
    private String opponentName;

    private double stake;

    private boolean open;

    public Challenge() {
    }

    public Challenge(int challengerId, String challengerName, int opponentId, String opponentName, double stake, boolean open) {
        this.challengerId = challengerId;
        this.challengerName = challengerName;
        this.opponentId = opponentId;
        this.opponentName = opponentName;
        this.stake = stake;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public int getChallengerId() {
        return challengerId;
    }

    public void setChallengerId(int challengerId) {
        this.challengerId = challengerId;
    }

    public String getChallengerName() {
        return challengerName;
    }

    public void setChallengerName(String challengerName) {
        this.challengerName = challengerName;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", challengerId=" + challengerId +
                ", challengerName='" + challengerName + '\'' +
                ", opponentId=" + opponentId +
                ", opponentName='" + opponentName + '\'' +
                ", stake=" + stake +
                ", open=" + open +
                '}';
    }
}
