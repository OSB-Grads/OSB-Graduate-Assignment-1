package com.bank.exception;

public class BankingException extends Exception {
    public BankingException(String message){
        super(message);
    }
    public BankingException(String message, Throwable cause){
        super(message, cause);
    }
    public BankingException(Throwable cause){
        super(cause);
    }
}
