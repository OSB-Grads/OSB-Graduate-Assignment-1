package com.bank.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Account information.
 * Used to transfer account data between layers.
 */
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Long userId;
    private String accountType; // SAVINGS or FIXED_DEPOSIT
    private BigDecimal balance;
    private boolean isLocked;
    private String createdAt;
    private String updatedAt;

    // Default constructor
    public AccountDTO() {}

    // Constructor with all fields
    public AccountDTO(Long id, String accountNumber, Long userId, String accountType, 
                     BigDecimal balance, boolean isLocked, String createdAt, String updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isLocked = isLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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
                "id=" + id +
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
