package com.bank.entity;
import java.time.LocalDate;

public class transactionEntity {

    /* Transaction Entity Properties */

   private int transaction_id;
   private int from_account_id;
   private int to_account_id;
   private String transaction_type;
   private double amount;
   private String description;
   private String status;
   private LocalDate created_at;

    /* Transaction Entity Constructor */

    public transactionEntity(int transaction_id,  int from_account_id, int to_account_id,String transaction_type, double amount, String description, String status, LocalDate created_at) {
        this.transaction_id = transaction_id;
        this.from_account_id = from_account_id;
        this.to_account_id = to_account_id;
        this.transaction_type=transaction_type;
        this.amount = amount;
        this.description = description;
        this.status = status;            /* HERE STATUS IS KEPT AS STRING BECAUSE IN DB WE DON'T HAVE ENUM */
        this.created_at = created_at;
    }

    /* Transaction Entity Getters and Setters */

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getFrom_account_id() {
        return from_account_id;
    }

    public void setFrom_account_id(int from_account_id) {
        this.from_account_id = from_account_id;
    }

    public int getTo_account_id() {
        return to_account_id;
    }

    public void setTo_account_id(int to_account_id) {
        this.to_account_id = to_account_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.created_at = createdAt;
    }
}
