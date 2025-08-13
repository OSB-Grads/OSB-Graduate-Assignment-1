package com.bank.dto;

import com.bank.db.LogDAO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
//import com.bank.entity.LogEntity;

public class LogDTO {
    private int id;
    private int user_id;
    private LogDAO.Action action;           // enum: Profile Management, Authentication, Transactions, Creation Management
    private String details;
    private String ipAddress;
    private LogDAO.Status status;            // enum: SUCCESS, FAILURE, ERROR
    private String type;                // enum: error, info, warn
    private Timestamp createdAt;

    public LogDTO(int id, int user_id, LogDAO.Action action, String details, String ipAddress, LogDAO.Status status, String type, Timestamp createdAt) {
        this.id = id;
        this.user_id = user_id;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
    }

    public LogDTO() {

    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public LogDAO.Action getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LogDAO.Status getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setAction(LogDAO.Action action) {
        this.action = action;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setStatus(LogDAO.Status status) {
        this.status = status;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "LogDTO{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}