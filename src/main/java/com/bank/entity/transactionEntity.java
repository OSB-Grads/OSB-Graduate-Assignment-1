package com.bank.entity;
import java.sql.Timestamp;

/**
 * Represents a transaction in the banking application.
 * This entity encapsulates all transaction-related data and is used for database mapping.
 */

public class transactionEntity {

    /* =======================
       Fields (Properties)
       ======================= */

   private int transaction_id;
   private int from_account_id;
   private int to_account_id;
   private String transaction_type;
   private double amount;
   private String description;
   private String status;
   private Timestamp created_at;


    /* =======================
       Constructors
       ======================= */

    /**
     * Full constructor for creating a TransactionEntity instance.
     */


    public transactionEntity() {
        this.transaction_id = transaction_id;
        this.from_account_id = from_account_id;
        this.to_account_id = to_account_id;
        this.transaction_type=transaction_type;
        this.amount = amount;
        this.description = description;
        this.status = status;            /* HERE STATUS IS KEPT AS STRING BECAUSE IN DB WE DON'T HAVE ENUM */
        this.created_at = created_at;
    }


   /* =======================
       Getters and Setters
       ======================= */


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

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.created_at = createdAt;
    }
}
