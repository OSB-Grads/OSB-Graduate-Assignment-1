package com.bank.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Data Transfer Object for Account information.
 * Used to transfer account data between layers.
 */
public class AccountDTO {
    private int accId;
    private String accountNumber;
    private int userId;
    private String accountType; // SAVINGS or FIXED_DEPOSIT
    private double balance;
    private boolean isLocked;
    private Timestamp createdAt;
    private Timestamp updatedAt;



    // Default constructor
    public AccountDTO() {}



    // Constructor with all fields

    public AccountDTO(String accountNumber, int accId, int userId, String accountType, double balance, boolean isLocked, Timestamp createdAt, Timestamp updatedAt) {
        this.accountNumber = accountNumber;
        this.accId = accId;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isLocked = isLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Getters and Setters


    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
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
