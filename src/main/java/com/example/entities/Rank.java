package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by admin on 02/03/2017.
 */
@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int userID;
    private int currentRank;
    private int bestRank;

    public Rank() {
    }

    public Rank(int userID, int currentRank, int bestRank) {
        this.userID = userID;
        this.currentRank = currentRank;
        this.bestRank = bestRank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public int getBestRank() {
        return bestRank;
    }

    public void setBestRank(int bestRank) {
        this.bestRank = bestRank;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", userID=" + userID +
                ", currentRank=" + currentRank +
                ", bestRank=" + bestRank +
                '}';
    }
}
