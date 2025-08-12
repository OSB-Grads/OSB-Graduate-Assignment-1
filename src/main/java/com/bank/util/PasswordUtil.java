package com.bank.util;
import org.mindrot.jbcrypt.BCrypt;
public class PasswordUtil {
    private static final int BCRYPT_ROUNDS = 12;
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }


        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
        System.out.println("Password hashed successfully");

        return hashedPassword;
    }


    public static boolean VerifyPassword(String plainPassword ,String hashedPassword){
        if(plainPassword==null || hashedPassword==null){
            return false;
        }
        boolean isValid;
         try {
             isValid=BCrypt.checkpw(plainPassword,hashedPassword);

         }catch(Exception e){
             System.err.println("error occred dyuring the password verfication:"+e.getMessage());
             return  false;
         }
         return isValid;

    }


}
