package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
/**
 * Created by admin on 09/02/2017.
 */

@Entity
public class FightEnd extends FightFactory{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Column(name="winID")
    private int winnerID;

    @Column(name="loseID")
    private int loserID;

    @Column(name="winVal")
    private double winAmount;




    public FightEnd(int winnerID, int loserID, double winAmount, Timestamp timestamp,
                    List<CurrencyPair> pairs, int challengerID, String challengerDirection,
                    double challengerStake, double challengerLeverage, double challengerBalance,
                    int opponentID, String opponentDirection, double opponentStake, double opponentLeverage, double opponentBalance) {

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

    public double getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(double winAmount) {
        this.winAmount = winAmount;
    }



}
