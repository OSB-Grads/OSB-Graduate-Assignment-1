package com.bank.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when attempting to withdraw or transfer more money than available.
 */
public class InsufficientFundsException extends BankingException {
    private  BigDecimal requestedAmount;
    private  BigDecimal availableBalance;
    
    public InsufficientFundsException(String message, BigDecimal requestedAmount, BigDecimal availableBalance) {
        super(message);
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }
    
    public InsufficientFundsException(String message, BigDecimal requestedAmount, BigDecimal availableBalance, Throwable cause) {
        super(message, cause);
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public InsufficientFundsException(String message) {
        super(message);
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
}
