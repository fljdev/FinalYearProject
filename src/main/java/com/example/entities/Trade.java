package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private CurrencyPair currencyPairOpen;


    @ManyToOne
    private CurrencyPair currencyPairClose;



    private Timestamp timestampOpen;
    private Timestamp timestampClose;
    private double margin;
    private String action;


    private boolean open;

    private double closingProfitLoss;

    private double positionUnits;

    @ManyToOne
    private Challenge challenge;




    public Trade() {
    }

    public Trade(User user, CurrencyPair currencyPairOpen, CurrencyPair currencyPairClose, Timestamp timestampOpen,
                 Timestamp timestampClose, double margin, String action, boolean open, double closingProfitLoss,
                 double positionUnits, Challenge challenge) {
        this.user = user;
        this.currencyPairOpen = currencyPairOpen;
        this.currencyPairClose = currencyPairClose;
        this.timestampOpen = timestampOpen;
        this.timestampClose = timestampClose;
        this.margin = margin;
        this.action = action;
        this.open = open;
        this.closingProfitLoss = closingProfitLoss;
        this.positionUnits = positionUnits;
        this.challenge = challenge;
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

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public double getClosingProfitLoss() {
        return closingProfitLoss;
    }

    public void setClosingProfitLoss(double closingProfitLoss) {
        this.closingProfitLoss = closingProfitLoss;
    }

    public double getPositionUnits() {
        return positionUnits;
    }

    public void setPositionUnits(double positionUnits) {
        this.positionUnits = positionUnits;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
