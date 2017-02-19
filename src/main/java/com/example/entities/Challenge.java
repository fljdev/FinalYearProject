package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by admin on 18/02/2017.
 */

@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int challengerId;
    private int opponentId;
    private boolean open;

    public Challenge() {
    }

    public Challenge(int challengerId, int opponentId, boolean open) {
        this.challengerId = challengerId;
        this.opponentId = opponentId;
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

    public int getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
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
                ", opponentId=" + opponentId +
                ", open=" + open +
                '}';
    }
}
