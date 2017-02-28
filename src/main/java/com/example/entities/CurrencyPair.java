package com.example.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by admin on 17/10/2016.
 */

@Entity
public class CurrencyPair {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String symbols;
    private long milliseconds;
    private double bid;
    private double ask;
    private double high;
    private double low;
    private double open;

    private double spreadPips;

    @Column(nullable = true)
    private boolean active = true;

    private String timeStampString;

    public CurrencyPair() {
    }

    public CurrencyPair(String symbols, long milliseconds, double bid, double ask, double high, double low,
                        double open, double spreadPips, boolean active, String timeStampString) {
        this.symbols = symbols;
        this.milliseconds = milliseconds;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.open = open;
        this.spreadPips = spreadPips;
        this.active = active;
        this.timeStampString = timeStampString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getSpreadPips() {
        return spreadPips;
    }

    public void setSpreadPips(double spreadPips) {
        this.spreadPips = spreadPips;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTimeStampString() {
        return timeStampString;
    }

    public void setTimeStampString(String timeStampString) {
        this.timeStampString = timeStampString;
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
                "id=" + id +
                ", symbols='" + symbols + '\'' +
                ", milliseconds=" + milliseconds +
                ", bid=" + bid +
                ", ask=" + ask +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", spreadPips=" + spreadPips +
                ", active=" + active +
                ", timeStampString='" + timeStampString + '\'' +
                '}';
    }
}
