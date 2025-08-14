package com.bank.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object for Account information.
 * Used to transfer account data between layers.
 */
public class AccountDTO {

    private String accountNumber;
    private int userId;
    private String accountType; // SAVINGS or FIXED_DEPOSIT
    private double balance;
    private boolean isLocked;
    private double interest;
    private String createdAt;
    private String updatedAt;


    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }




    // Default constructor
    public AccountDTO() {}



    // Constructor with all fields

    public AccountDTO(String accountNumber, int userId, String accountType, double balance, boolean isLocked, String createdAt, String updatedAt,double interest) {
        this.accountNumber = accountNumber;
        this.interest=interest;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isLocked = isLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters




    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId=" + userId +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", isLocked=" + isLocked +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
