package com.example.entities;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by admin on 09/02/2017.
 */

@Entity
public class FightStart extends FightFactory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public FightStart(){

    }

    public FightStart(Timestamp timestamp, Set<CurrencyPair> pairs, int challengerID,
                      String challengerDirection, double challengerStake, double challengerLeverage,
                      double challengerBalance, int opponentID, String opponentDirection,
                      double opponentStake, double opponentLeverage, double opponentBalance) {
      super();
    }


    public int getId() {
        return id;
    }
}
