package com.bank.exception;

public class TransactionFailureException extends BankingException{
    public TransactionFailureException(String message) {
        super(message);
    }

    public TransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionFailureException(Throwable cause) {
        super(cause);
    }
}
