package com.bank.entity;

import java.time.LocalDate;

public class LogEntity {
    private int id;
    private int user_id;
    private String action;
    private String details;
    private String ip_address;
    private String status;
    private LocalDate timestamp;


    public LogEntity(){}


    public LogEntity(int id, int user_id, String action, String details, String ip_address, String status, LocalDate timestamp) {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }





}
