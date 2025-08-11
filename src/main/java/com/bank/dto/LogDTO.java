package com.bank.dto;

import java.time.LocalDateTime;
//import com.bank.entity.LogEntity;

public class LogDTO {
    private int id;
    private int user_id;
    private String action;           // enum: Profile Management, Authentication, Transactions, Creation Management
    private String details;
    private String ipAddress;
    private String status;            // enum: SUCCESS, FAILURE, ERROR
    private String type;                // enum: error, info, warn
    private LocalDateTime createdAt;

    public LogDTO(int id, int user_id, String action, String details, String ipAddress, String status, String type, LocalDateTime createdAt) {
        this.id = id;
        this.user_id = user_id;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}