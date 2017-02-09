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
public class FightStart extends AbstractFight{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    public FightStart(Timestamp timestamp, Set<CurrencyPair> pairs, int challengerID, String challengerDirection,
                      double challengerStake, double challengerLeverage, double challengerBalance, int opponentID,
                      String opponentDirection, double opponentStake, double opponentLeverage, double opponentBalance) {

        super(timestamp, pairs, challengerID, challengerDirection, challengerStake, challengerLeverage,
                challengerBalance, opponentID, opponentDirection, opponentStake, opponentLeverage, opponentBalance);
    }
}
