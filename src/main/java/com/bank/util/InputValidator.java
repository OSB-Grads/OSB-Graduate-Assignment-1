package com.bank.util;

import java.util.regex.Pattern;

public class InputValidator {
    public static boolean isValidUsername(String username) {
        if(username==null){
            return  false;
        }
        String regex="^[A-Za-z0-9]{5,}$";
        return username.matches(regex);
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        // At least 8 characters, 1 digit, 1 special character
        String regex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";
        return password.matches(regex);
    }

    public static boolean isValidFullName(String fullName) {
        if (fullName == null) return false;
        return fullName.trim().matches("^[A-Za-z]{2,}( [A-Za-z]{2,})*$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("\\d{10}");
    }
}
