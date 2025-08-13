package com.bank.entity;

import java.sql.Timestamp;


public class AccountEntity {

    private String account_number;
    private int User_id;
    private String account_type;
    private double balance;
    private boolean is_locked;
    private Timestamp account_created;
    private Timestamp account_updated;
    private String type;
    private double interest;




    public AccountEntity() {

    }


    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public AccountEntity( String account_number, int user_id, String account_type, double balance, boolean is_locked, Timestamp account_created, Timestamp account_updated, String type, double interest) {

        this.account_number = account_number;
        User_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
        this.is_locked = is_locked;
        this.account_created = account_created;
        this.account_updated = account_updated;
        this.type = type;
        this.interest = interest;
    }



    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public Timestamp getAccount_created() {
        return account_created;
    }

    public void setAccount_created(Timestamp account_created) {
        this.account_created = account_created;
    }

    public Timestamp getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(Timestamp account_updated) {
        this.account_updated = account_updated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double intrest) {
        this.interest = intrest;
    }


}
