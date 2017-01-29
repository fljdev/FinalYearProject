package com.example.forex;

/**
 * Created by admin on 17/10/2016.
 */
public class CurrencyPair {

    private String symbols;
    private long milliseconds;
    private double bid;
    private double ask;
    private double high;
    private double low;
    private double open;



    public CurrencyPair(String symbols,long milliseconds, double bid, double ask, double high, double low, double open) {
        this.symbols = symbols;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.open = open;
    }

    public CurrencyPair() {
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


    @Override
    public String toString() {
        return "CurrencyPair{" +
                "symbols='" + symbols + '\'' +
                ", milliseconds=" + milliseconds +
                ", bid=" + bid +
                ", ask=" + ask +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                '}';
    }
}
