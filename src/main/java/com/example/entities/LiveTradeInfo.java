package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by admin on 08/04/2017.
 */
@Entity
public class LiveTradeInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double currentProfitAndLoss;
    private double currentBid;
    private double currentAsk;

    private Timestamp tickTime;

    private int  tradeID;

    public LiveTradeInfo() {
    }

    public LiveTradeInfo(double currentProfitAndLoss, double currentBid, double currentAsk, Timestamp tickTime, int tradeID) {
        this.currentProfitAndLoss = currentProfitAndLoss;
        this.currentBid = currentBid;
        this.currentAsk = currentAsk;
        this.tickTime = tickTime;
        this.tradeID = tradeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCurrentProfitAndLoss() {
        return currentProfitAndLoss;
    }

    public void setCurrentProfitAndLoss(double currentProfitAndLoss) {
        this.currentProfitAndLoss = currentProfitAndLoss;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public double getCurrentAsk() {
        return currentAsk;
    }

    public void setCurrentAsk(double currentAsk) {
        this.currentAsk = currentAsk;
    }

    public Timestamp getTickTime() {
        return tickTime;
    }

    public void setTickTime(Timestamp tickTime) {
        this.tickTime = tickTime;
    }

    public int getTradeID() {
        return tradeID;
    }

    public void setTradeID(int tradeID) {
        this.tradeID = tradeID;
    }


    @Override
    public String toString() {
        return "LiveTradeInfo{" +
                "id=" + id +
                ", currentProfitAndLoss=" + currentProfitAndLoss +
                ", currentBid=" + currentBid +
                ", currentAsk=" + currentAsk +
                ", tickTime=" + tickTime +
                ", tradeID=" + tradeID +
                '}';
    }
}
