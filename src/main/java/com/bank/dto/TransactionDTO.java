package com.bank.dto;


public class TransactionDTO {


    private String transaction_id;
    private String from_account_id;
    private String to_account_id;
    private String transaction_type;
    private double amount;
    private String description;
    private String status;
    private String created_at;

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getFrom_account_id() {
        return from_account_id;
    }

    public String getTo_account_id() {
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

    public String getCreated_at() {
        return created_at;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setFrom_account_id(String from_account_id) {
        this.from_account_id = from_account_id;
    }

    public void setTo_account_id(String to_account_id) {
        this.to_account_id = to_account_id;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public TransactionDTO(String transaction_id, String from_account_id, String to_account_id, String transaction_type, double amount, String description, String status, String created_at) {
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
