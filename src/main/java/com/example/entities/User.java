package com.example.entities;


import javax.persistence.*;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private BankAccount account;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private boolean online;
    private boolean busy;

    private int currentRank;
    private int bestRank;

    private double currentProfit;
    private double totalMargin;

    @ManyToOne
    private GameAccount gameAccount;

    public User() {
    }

    public User(BankAccount account, String firstName, String lastName, String username, String email, String password,
                boolean online, boolean busy, int currentRank, int bestRank,
                double currentProfit, double totalMargin, GameAccount gameAccount) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.online = online;
        this.busy = busy;
        this.currentRank = currentRank;
        this.bestRank = bestRank;
        this.currentProfit = currentProfit;
        this.totalMargin = totalMargin;
        this.gameAccount = gameAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
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

    public double getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(double currentProfit) {
        this.currentProfit = currentProfit;
    }

    public double getTotalMargin() {
        return totalMargin;
    }

    public void setTotalMargin(double totalMargin) {
        this.totalMargin = totalMargin;
    }

    public GameAccount getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(GameAccount gameAccount) {
        this.gameAccount = gameAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account=" + account +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", online=" + online +
                ", busy=" + busy +
                ", currentRank=" + currentRank +
                ", bestRank=" + bestRank +
                ", currentProfit=" + currentProfit +
                ", totalMargin=" + totalMargin +
                ", gameAccount=" + gameAccount +
                '}';
    }
}