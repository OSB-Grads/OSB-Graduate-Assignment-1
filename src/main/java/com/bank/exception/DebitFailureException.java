package com.bank.exception;

public class DebitFailureException extends  BankingException{
    public DebitFailureException(String message) {
        super(message);
    }

    public DebitFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public DebitFailureException(Throwable cause) {
        super(cause);
    }
}
