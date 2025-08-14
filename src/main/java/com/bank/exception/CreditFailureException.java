package com.bank.exception;

public class CreditFailureException extends BankingException{
    public CreditFailureException(String message) {
        super(message);
    }

    public CreditFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditFailureException(Throwable cause) {
        super(cause);
    }
}
