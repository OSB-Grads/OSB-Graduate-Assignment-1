package com.bank.exception;

public class UserNotfoundException extends BankingException{
    public UserNotfoundException(){super("UserName Is InCorrect Or Does Not Exist.Please Try Again");}

    public UserNotfoundException(String message) {
        super(message);
    }

    public UserNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
