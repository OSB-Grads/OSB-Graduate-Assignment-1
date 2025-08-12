package com.bank.dto;

import java.sql.Timestamp;
import java.time.LocalDate;

public class TransactionDTO {


    private int transaction_id;
    private int from_account_id;
    private int to_account_id;
    private String transaction_type;
    private double amount;
    private String description;
    private String status;
    private Timestamp  created_at;

    public int getTransaction_id() {
        return transaction_id;
    }

    public int getFrom_account_id() {
        return from_account_id;
    }

    public int getTo_account_id() {
        return to_account_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public TransactionDTO(int transaction_id, int from_account_id, int to_account_id, String transaction_type, double amount, String description, String status, Timestamp created_at) {
        this.transaction_id = transaction_id;
        this.from_account_id = from_account_id;
        this.to_account_id = to_account_id;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.created_at = created_at;
    }
    public TransactionDTO(){

    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "transaction_id=" + transaction_id +
                ", from_account_id=" + from_account_id +
                ", to_account_id=" + to_account_id +
                ", transaction_type='" + transaction_type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
