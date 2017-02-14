package com.example.forex;

import com.example.entities.CurrencyPair;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by admin on 16/01/2017.
 */

public class CurrencyPairSet {



    public CurrencyPairSet(){

    }


    private CurrencyPair EURUSD;//.17
    private CurrencyPair USDJPY;//.17
    private CurrencyPair GBPUSD;//.17
    private CurrencyPair EURGBP;//.17
    private CurrencyPair USDCHF;//.17
    private CurrencyPair EURJPY;//.17
    private CurrencyPair EURCHF;//.25
    private CurrencyPair USDCAD;//.17
    private CurrencyPair AUDUSD;//.17
    private CurrencyPair GBPJPY;//.17
    private CurrencyPair AUDCAD;//.25
    private CurrencyPair AUDCHF;//.25
    private CurrencyPair AUDJPY;//.25
    private CurrencyPair AUDNZD;//.25
    private CurrencyPair CADCHF;//.25
    private CurrencyPair CADJPY;//.25
    private CurrencyPair CHFJPY;//.17
    private CurrencyPair EURAUD;//.17
    private CurrencyPair EURCAD;//.25
    private CurrencyPair EURNOK;//.25
    private CurrencyPair EURNZD;//.17
    private CurrencyPair GBPCAD;//.17
    private CurrencyPair GBPCHF;//.25
    private CurrencyPair NZDJPY;//.25
    private CurrencyPair NZDUSD;//.17
    private CurrencyPair USDNOK;//.25
    private CurrencyPair USDSEK;//.25

    public CurrencyPair getEURUSD() {
        return EURUSD;
    }

    public void setEURUSD(CurrencyPair EURUSD) {
        this.EURUSD = EURUSD;
    }

    public CurrencyPair getUSDJPY() {
        return USDJPY;
    }

    public void setUSDJPY(CurrencyPair USDJPY) {
        this.USDJPY = USDJPY;
    }

    public CurrencyPair getGBPUSD() {
        return GBPUSD;
    }

    public void setGBPUSD(CurrencyPair GBPUSD) {
        this.GBPUSD = GBPUSD;
    }

    public CurrencyPair getEURGBP() {
        return EURGBP;
    }

    public void setEURGBP(CurrencyPair EURGBP) {
        this.EURGBP = EURGBP;
    }

    public CurrencyPair getUSDCHF() {
        return USDCHF;
    }

    public void setUSDCHF(CurrencyPair USDCHF) {
        this.USDCHF = USDCHF;
    }

    public CurrencyPair getEURJPY() {
        return EURJPY;
    }

    public void setEURJPY(CurrencyPair EURJPY) {
        this.EURJPY = EURJPY;
    }

    public CurrencyPair getEURCHF() {
        return EURCHF;
    }

    public void setEURCHF(CurrencyPair EURCHF) {
        this.EURCHF = EURCHF;
    }

    public CurrencyPair getUSDCAD() {
        return USDCAD;
    }

    public void setUSDCAD(CurrencyPair USDCAD) {
        this.USDCAD = USDCAD;
    }

    public CurrencyPair getAUDUSD() {
        return AUDUSD;
    }

    public void setAUDUSD(CurrencyPair AUDUSD) {
        this.AUDUSD = AUDUSD;
    }

    public CurrencyPair getGBPJPY() {
        return GBPJPY;
    }

    public void setGBPJPY(CurrencyPair GBPJPY) {
        this.GBPJPY = GBPJPY;
    }

    public CurrencyPair getAUDCAD() {
        return AUDCAD;
    }

    public void setAUDCAD(CurrencyPair AUDCAD) {
        this.AUDCAD = AUDCAD;
    }

    public CurrencyPair getAUDCHF() {
        return AUDCHF;
    }

    public void setAUDCHF(CurrencyPair AUDCHF) {
        this.AUDCHF = AUDCHF;
    }

    public CurrencyPair getAUDJPY() {
        return AUDJPY;
    }

    public void setAUDJPY(CurrencyPair AUDJPY) {
        this.AUDJPY = AUDJPY;
    }

    public CurrencyPair getAUDNZD() {
        return AUDNZD;
    }

    public void setAUDNZD(CurrencyPair AUDNZD) {
        this.AUDNZD = AUDNZD;
    }

    public CurrencyPair getCADCHF() {
        return CADCHF;
    }

    public void setCADCHF(CurrencyPair CADCHF) {
        this.CADCHF = CADCHF;
    }

    public CurrencyPair getCADJPY() {
        return CADJPY;
    }

    public void setCADJPY(CurrencyPair CADJPY) {
        this.CADJPY = CADJPY;
    }

    public CurrencyPair getCHFJPY() {
        return CHFJPY;
    }

    public void setCHFJPY(CurrencyPair CHFJPY) {
        this.CHFJPY = CHFJPY;
    }

    public CurrencyPair getEURAUD() {
        return EURAUD;
    }

    public void setEURAUD(CurrencyPair EURAUD) {
        this.EURAUD = EURAUD;
    }

    public CurrencyPair getEURCAD() {
        return EURCAD;
    }

    public void setEURCAD(CurrencyPair EURCAD) {
        this.EURCAD = EURCAD;
    }

    public CurrencyPair getEURNOK() {
        return EURNOK;
    }

    public void setEURNOK(CurrencyPair EURNOK) {
        this.EURNOK = EURNOK;
    }

    public CurrencyPair getEURNZD() {
        return EURNZD;
    }

    public void setEURNZD(CurrencyPair EURNZD) {
        this.EURNZD = EURNZD;
    }

    public CurrencyPair getGBPCAD() {
        return GBPCAD;
    }

    public void setGBPCAD(CurrencyPair GBPCAD) {
        this.GBPCAD = GBPCAD;
    }

    public CurrencyPair getGBPCHF() {
        return GBPCHF;
    }

    public void setGBPCHF(CurrencyPair GBPCHF) {
        this.GBPCHF = GBPCHF;
    }

    public CurrencyPair getNZDJPY() {
        return NZDJPY;
    }

    public void setNZDJPY(CurrencyPair NZDJPY) {
        this.NZDJPY = NZDJPY;
    }

    public CurrencyPair getNZDUSD() {
        return NZDUSD;
    }

    public void setNZDUSD(CurrencyPair NZDUSD) {
        this.NZDUSD = NZDUSD;
    }

    public CurrencyPair getUSDNOK() {
        return USDNOK;
    }

    public void setUSDNOK(CurrencyPair USDNOK) {
        this.USDNOK = USDNOK;
    }

    public CurrencyPair getUSDSEK() {
        return USDSEK;
    }

    public void setUSDSEK(CurrencyPair USDSEK) {
        this.USDSEK = USDSEK;
    }

    @Override
    public String toString() {
        return "CurrencyPairSet{" +
                "EURUSD=" + EURUSD +
                ", USDJPY=" + USDJPY +
                ", GBPUSD=" + GBPUSD +
                ", EURGBP=" + EURGBP +
                ", USDCHF=" + USDCHF +
                ", EURJPY=" + EURJPY +
                ", EURCHF=" + EURCHF +
                ", USDCAD=" + USDCAD +
                ", AUDUSD=" + AUDUSD +
                ", GBPJPY=" + GBPJPY +
                ", AUDCAD=" + AUDCAD +
                ", AUDCHF=" + AUDCHF +
                ", AUDJPY=" + AUDJPY +
                ", AUDNZD=" + AUDNZD +
                ", CADCHF=" + CADCHF +
                ", CADJPY=" + CADJPY +
                ", CHFJPY=" + CHFJPY +
                ", EURAUD=" + EURAUD +
                ", EURCAD=" + EURCAD +
                ", EURNOK=" + EURNOK +
                ", EURNZD=" + EURNZD +
                ", GBPCAD=" + GBPCAD +
                ", GBPCHF=" + GBPCHF +
                ", NZDJPY=" + NZDJPY +
                ", NZDUSD=" + NZDUSD +
                ", USDNOK=" + USDNOK +
                ", USDSEK=" + USDSEK +
                '}';
    }
}
