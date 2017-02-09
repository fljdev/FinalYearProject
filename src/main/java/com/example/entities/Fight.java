package com.example.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by admin on 07/02/2017.
 */

@Entity
public class Fight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int challengerID;

    @ManyToMany
    private Set<CurrencyPair> pairs;


    private String challengerDirection;
    private double challengerStake;
    private double challengerLeverage;

    private int opponentID;
    private String opponentDirection;
    private double opponentStake;
    private double opponentLeverage;

    public Fight() {
    }


    public Fight(int challengerID, Set<CurrencyPair> pairs, String challengerDirection, double challengerStake, double challengerLeverage, int opponentID, String opponentDirection, double opponentStake, double opponentLeverage) {
        this.challengerID = challengerID;
        this.pairs = pairs;
        this.challengerDirection = challengerDirection;
        this.challengerStake = challengerStake;
        this.challengerLeverage = challengerLeverage;
        this.opponentID = opponentID;
        this.opponentDirection = opponentDirection;
        this.opponentStake = opponentStake;
        this.opponentLeverage = opponentLeverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChallengerID() {
        return challengerID;
    }

    public void setChallengerID(int challengerID) {
        this.challengerID = challengerID;
    }

    public Set<CurrencyPair> getPairs() {
        return pairs;
    }

    public void setPairs(Set<CurrencyPair> pairs) {
        this.pairs = pairs;
    }

    public String getChallengerDirection() {
        return challengerDirection;
    }

    public void setChallengerDirection(String challengerDirection) {
        this.challengerDirection = challengerDirection;
    }

    public double getChallengerStake() {
        return challengerStake;
    }

    public void setChallengerStake(double challengerStake) {
        this.challengerStake = challengerStake;
    }

    public double getChallengerLeverage() {
        return challengerLeverage;
    }

    public void setChallengerLeverage(double challengerLeverage) {
        this.challengerLeverage = challengerLeverage;
    }

    public int getOpponentID() {
        return opponentID;
    }

    public void setOpponentID(int opponentID) {
        this.opponentID = opponentID;
    }

    public String getOpponentDirection() {
        return opponentDirection;
    }

    public void setOpponentDirection(String opponentDirection) {
        this.opponentDirection = opponentDirection;
    }

    public double getOpponentStake() {
        return opponentStake;
    }

    public void setOpponentStake(double opponentStake) {
        this.opponentStake = opponentStake;
    }

    public double getOpponentLeverage() {
        return opponentLeverage;
    }

    public void setOpponentLeverage(double opponentLeverage) {
        this.opponentLeverage = opponentLeverage;
    }

    @Override
    public String toString() {
        return "Fight{" +
                "id=" + id +
                ", challengerID=" + challengerID +
                ", pairs=" + pairs +
                ", challengerDirection='" + challengerDirection + '\'' +
                ", challengerStake=" + challengerStake +
                ", challengerLeverage=" + challengerLeverage +
                ", opponentID=" + opponentID +
                ", opponentDirection='" + opponentDirection + '\'' +
                ", opponentStake=" + opponentStake +
                ", opponentLeverage=" + opponentLeverage +
                '}';
    }
}
