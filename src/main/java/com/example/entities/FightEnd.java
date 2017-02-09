package com.example.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by admin on 09/02/2017.
 */

@Entity
public class FightEnd extends AbstractFight{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private int winnerID;
    private int loserID;
    private double winAmount;

    public FightEnd(Timestamp timestamp, Set<CurrencyPair> pairs, int challengerID, String challengerDirection,
                    double challengerStake, double challengerLeverage, double challengerBalance, int opponentID,
                    String opponentDirection, double opponentStake, double opponentLeverage, double opponentBalance,
                     int winnerID, int loserID, double winAmount) {
        super(timestamp, pairs, challengerID, challengerDirection, challengerStake, challengerLeverage, challengerBalance,
                opponentID, opponentDirection, opponentStake, opponentLeverage, opponentBalance);

        this.winnerID = winnerID;
        this.loserID = loserID;
        this.winAmount = winAmount;
    }

}
