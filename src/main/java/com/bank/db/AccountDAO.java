package com.bank.db;

import com.bank.entity.AccountEntity;
import com.bank.entity.LogEntity;
import com.bank.mapper.AccountMapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AccountDAO {

    public enum AccountType {
        SAVINGS,
        FIXED_DEPOSIT
    }

    private final DatabaseManager db;


    public AccountDAO(DatabaseManager db) {
        this.db = db;
    }


    public void insertAccountDetails(AccountEntity accountEntity) {
        try {
            String sql = String.format(
                    "INSERT INTO accounts(account_number,user_id,account_type,is_locked) VALUES('%s','%d','%s','%b')",
                    accountEntity.getAccount_number(),
                    accountEntity.getUser_id(),
                    accountEntity.getAccount_type(),
                    accountEntity.isIs_locked()
            );
            db.query(sql);
        } catch (Exception e) {
            System.out.println("Error while executing the Acoount Details " + e);
        }

    }

    public List<AccountEntity> getAccountDetails() {
        List<AccountEntity> result = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try {
            List<Map<String, Object>> rows = db.query(sql);
            for (Map<String, Object> row : rows) {
                AccountEntity account = new AccountEntity();
                account.setAccount_number((String) row.get("account_number"));
                account.setUser_id((Integer) row.get("user_id"));
                account.setAccount_type(String.valueOf(AccountType.valueOf((String) row.get("account_type"))));
                result.add(account);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving account details: " + e);
        }
        return result;
    }


    // Get account by ID
    public AccountEntity getAccountById(String accountNumber) {
        AccountEntity account = new AccountEntity();
        String sql = "SELECT * FROM accounts WHERE account_number = " + accountNumber;

        try {
            List<Map<String, Object>> rows = db.query(sql);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.get(0); // Since account IDs are unique, we expect a single row
                account=AccountMapper.mapToAccountEntity(row);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving account by ID: " + e);
        }

        return account;
    }

    // Update account details

    public void updateAccountDetails(AccountEntity accountEntity) {
        String sql = String.format(
                "UPDATE accounts SET " +
                        "balance = '%.2f', " +
                        "is_locked = '%b', " +
                        "updated_at = CURRENT_TIMESTAMP " + // Automatically update the timestamp
                        "WHERE account_number = '%s'",
                accountEntity.getBalance(),
                accountEntity.isIs_locked(),
                accountEntity.getAccount_number()
        );

        try {
            db.query(sql);
        } catch (Exception e) {
            System.out.println("Error while updating account details: " + e);
        }
    }
    public List<AccountEntity> getAccountsByUserId(int  Userid){

        String sql="Select * from accounts where user_id="+Userid;
        try {
            List<Map<String, Object>> rows = db.query(sql);
            return AccountMapper.mapToAccountEntityList(rows);
        }catch (Exception e){
            System.out.println("Error Retriving the userid");
            return new ArrayList<>();
        }

    }


}
