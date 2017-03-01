package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public Trade() {
    }

    @ManyToOne
    private User user;

    @ManyToOne
    private CurrencyPair currencyPairOpen;


    @ManyToOne
    private CurrencyPair currencyPairClose;



    private Timestamp timestampOpen;
    private Timestamp timestampClose;
    private double stake;
    private String action;

    @Column(nullable = true)
    private double profitLoss;

    public Trade(User user, CurrencyPair currencyPairOpen, CurrencyPair currencyPairClose,
                 Timestamp timestampOpen, Timestamp timestampClose, double stake, String action, double profitLoss) {
        this.user = user;
        this.currencyPairOpen = currencyPairOpen;
        this.currencyPairClose = currencyPairClose;
        this.timestampOpen = timestampOpen;
        this.timestampClose = timestampClose;
        this.stake = stake;
        this.action = action;
        this.profitLoss = profitLoss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CurrencyPair getCurrencyPairOpen() {
        return currencyPairOpen;
    }

    public void setCurrencyPairOpen(CurrencyPair currencyPairOpen) {
        this.currencyPairOpen = currencyPairOpen;
    }

    public CurrencyPair getCurrencyPairClose() {
        return currencyPairClose;
    }

    public void setCurrencyPairClose(CurrencyPair currencyPairClose) {
        this.currencyPairClose = currencyPairClose;
    }

    public Timestamp getTimestampOpen() {
        return timestampOpen;
    }

    public void setTimestampOpen(Timestamp timestampOpen) {
        this.timestampOpen = timestampOpen;
    }

    public Timestamp getTimestampClose() {
        return timestampClose;
    }

    public void setTimestampClose(Timestamp timestampClose) {
        this.timestampClose = timestampClose;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", user=" + user +
                ", currencyPairOpen=" + currencyPairOpen +
                ", currencyPairClose=" + currencyPairClose +
                ", timestampOpen=" + timestampOpen +
                ", timestampClose=" + timestampClose +
                ", stake=" + stake +
                ", action='" + action + '\'' +
                ", profitLoss=" + profitLoss +
                '}';
    }
}
