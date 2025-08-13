package com.bank.exception;

public class UserNotfoundException extends BankingException{
    public UserNotfoundException(){super("User Does Not Exist!");}

    public UserNotfoundException(String message) {
        super(message);
    }

    public UserNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
