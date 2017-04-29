package com.example.entities;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "Chall_ID")
    private int challengerId;
    @Column(name = "Chall")
    private String challengerName;

    @Column(name = "OPP_ID")
    private int opponentId;
    @Column(name = "Opp")
    private String opponentName;

    @Column(name = "length")
    private int duration;

    @Column(name = "$")
    private double stake;

    @Column(name = "Sent_Time")
    private String challengeSent;

    @Column(name = "Accept_Time")
    private String challengeAccepted;

    @Column(name = "Decline_Time")
    private String challengeDeclined;

    @Column(name = "Withdraw_Time")
    private String challengeWithdrawen;

    private double challengerProfit;

    private double opponentProfit;

    private boolean accepted;

    private boolean declined;

    private boolean withdrawen;

    private boolean open;

    private boolean completed;

    public Challenge() {
    }

    public Challenge(int challengerId, String challengerName, int opponentId, String opponentName, int duration,
                     double stake, String challengeSent, String challengeAccepted,
                     String challengeDeclined, String challengeWithdrawen, double challengerProfit,
                     double opponentProfit, boolean accepted, boolean declined, boolean withdrawen,
                     boolean open, boolean completed) {
        this.challengerId = challengerId;
        this.challengerName = challengerName;
        this.opponentId = opponentId;
        this.opponentName = opponentName;
        this.duration = duration;
        this.stake = stake;
        this.challengeSent = challengeSent;
        this.challengeAccepted = challengeAccepted;
        this.challengeDeclined = challengeDeclined;
        this.challengeWithdrawen = challengeWithdrawen;
        this.challengerProfit = challengerProfit;
        this.opponentProfit = opponentProfit;
        this.accepted = accepted;
        this.declined = declined;
        this.withdrawen = withdrawen;
        this.open = open;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChallengerId() {
        return challengerId;
    }

    public void setChallengerId(int challengerId) {
        this.challengerId = challengerId;
    }

    public String getChallengerName() {
        return challengerName;
    }

    public void setChallengerName(String challengerName) {
        this.challengerName = challengerName;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public String getChallengeSent() {
        return challengeSent;
    }

    public void setChallengeSent(String challengeSent) {
        this.challengeSent = challengeSent;
    }

    public String getChallengeAccepted() {
        return challengeAccepted;
    }

    public void setChallengeAccepted(String challengeAccepted) {
        this.challengeAccepted = challengeAccepted;
    }

    public String getChallengeDeclined() {
        return challengeDeclined;
    }

    public void setChallengeDeclined(String challengeDeclined) {
        this.challengeDeclined = challengeDeclined;
    }

    public String getChallengeWithdrawen() {
        return challengeWithdrawen;
    }

    public void setChallengeWithdrawen(String challengeWithdrawen) {
        this.challengeWithdrawen = challengeWithdrawen;
    }

    public double getChallengerProfit() {
        return challengerProfit;
    }

    public void setChallengerProfit(double challengerProfit) {
        this.challengerProfit = challengerProfit;
    }

    public double getOpponentProfit() {
        return opponentProfit;
    }

    public void setOpponentProfit(double opponentProfit) {
        this.opponentProfit = opponentProfit;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }

    public boolean isWithdrawen() {
        return withdrawen;
    }

    public void setWithdrawen(boolean withdrawen) {
        this.withdrawen = withdrawen;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", challengerId=" + challengerId +
                ", challengerName='" + challengerName + '\'' +
                ", opponentId=" + opponentId +
                ", opponentName='" + opponentName + '\'' +
                ", duration=" + duration +
                ", stake=" + stake +
                ", challengeSent='" + challengeSent + '\'' +
                ", challengeAccepted='" + challengeAccepted + '\'' +
                ", challengeDeclined='" + challengeDeclined + '\'' +
                ", challengeWithdrawen='" + challengeWithdrawen + '\'' +
                ", challengerProfit=" + challengerProfit +
                ", opponentProfit=" + opponentProfit +
                ", accepted=" + accepted +
                ", declined=" + declined +
                ", withdrawen=" + withdrawen +
                ", open=" + open +
                ", completed=" + completed +
                '}';
    }
}
