package com.example.entities;

import javax.persistence.*;

/**
 * Created by admin on 02/03/2017.
 */
@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int currentRank;

    @Column(nullable = true)
    private int bestRank;



    @OneToOne
    private User user;

    public Rank() {
    }

    public Rank(int currentRank, int bestRank, User user) {
        this.currentRank = currentRank;
        this.bestRank = bestRank;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", currentRank=" + currentRank +
                ", bestRank=" + bestRank +
                ", user=" + user +
                '}';
    }
}
