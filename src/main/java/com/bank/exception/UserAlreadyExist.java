package com.bank.exception;

public class UserAlreadyExist extends BankingException{
    public UserAlreadyExist(){
        super("User Already Exist.");
    }

    public UserAlreadyExist(String message) {
        super(message);
    }

    public UserAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
