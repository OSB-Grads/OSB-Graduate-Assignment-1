package com.bank.entity;

import com.bank.db.LogDAO;

import java.sql.Timestamp;
import java.time.LocalDate;

public class LogEntity {
    private int id;
    private int user_id;
    private LogDAO.Action action;
    private String details;
    private String ip_address;
    private LogDAO.Status status;
    private Timestamp timestamp;


    public LogEntity(){}


    public LogEntity(int id, int user_id, LogDAO.Action action, String details, String ip_address, LogDAO.Status status, Timestamp timestamp) {
        this.id = id;
        this.user_id = user_id;
        this.action = action;
        this.details = details;
        this.ip_address = ip_address;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LogDAO.Action getAction() {
        return action;
    }

    public void setAction(LogDAO.Action action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public LogDAO.Status getStatus() {
        return status;
    }

    public void setStatus(LogDAO.Status status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }





}
