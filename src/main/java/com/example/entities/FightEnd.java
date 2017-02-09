package com.example.entities;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by admin on 09/02/2017.
 */

@Entity
public class FightEnd{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private int winnerID;
    private int loserID;
    private double winAmount;

    private Timestamp timestamp;

    @ManyToMany
    private Set<CurrencyPair> pairs;

    private int challengerID;
    private String challengerDirection;
    private double challengerStake;
    private double challengerLeverage;
    private double challengerBalance;

    private int opponentID;
    private String opponentDirection;
    private double opponentStake;
    private double opponentLeverage;
    private double opponentBalance;

    public FightEnd(int winnerID, int loserID, double winAmount, Timestamp timestamp,
                    Set<CurrencyPair> pairs, int challengerID, String challengerDirection,
                    double challengerStake, double challengerLeverage, double challengerBalance,
                    int opponentID, String opponentDirection, double opponentStake, double opponentLeverage, double opponentBalance) {
        this.winnerID = winnerID;
        this.loserID = loserID;
        this.winAmount = winAmount;
        this.timestamp = timestamp;
        this.pairs = pairs;
        this.challengerID = challengerID;
        this.challengerDirection = challengerDirection;
        this.challengerStake = challengerStake;
        this.challengerLeverage = challengerLeverage;
        this.challengerBalance = challengerBalance;
        this.opponentID = opponentID;
        this.opponentDirection = opponentDirection;
        this.opponentStake = opponentStake;
        this.opponentLeverage = opponentLeverage;
        this.opponentBalance = opponentBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(double winAmount) {
        this.winAmount = winAmount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Set<CurrencyPair> getPairs() {
        return pairs;
    }

    public void setPairs(Set<CurrencyPair> pairs) {
        this.pairs = pairs;
    }

    public int getChallengerID() {
        return challengerID;
    }

    public void setChallengerID(int challengerID) {
        this.challengerID = challengerID;
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

    public double getChallengerBalance() {
        return challengerBalance;
    }

    public void setChallengerBalance(double challengerBalance) {
        this.challengerBalance = challengerBalance;
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

    public double getOpponentBalance() {
        return opponentBalance;
    }

    public void setOpponentBalance(double opponentBalance) {
        this.opponentBalance = opponentBalance;
    }
}
