package com.bank.exception;

/**
 * Exception thrown when user provides invalid login credentials.
 */
public class InvalidCredentialsException extends BankingException {
    public InvalidCredentialsException(){
        super("Invalid username or password.");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
