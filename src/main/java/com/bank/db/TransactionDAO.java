package com.bank.db;
import com.bank.entity.TransactionEntity;
import com.bank.mapper.TransactionMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TransactionDAO {
    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER, TRANSACT
    }
    public enum Status{
        SUCCESS, FAILURE, ERROR
    }

    private final DatabaseManager dm;
    public TransactionDAO(DatabaseManager dm) {
        this.dm = dm;
    }
    public List<TransactionEntity> getAllAccounts() throws SQLException {
        List<TransactionEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            while (rs.next()) {
                TransactionEntity ts = TransactionMapper.mapResultSetToEntity(rs);
                list.add(ts);
            }
        }
        return list;
    }

    public TransactionEntity getAccountById(String accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = '" + accountId + "'";
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            if (rs.next()) {
                return TransactionMapper.mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    public List<TransactionEntity> getAccountsByUserID(int userId) throws SQLException {
        List<TransactionEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account_id = " + userId + " OR to_account_id = " + userId;
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            while (rs.next()) {
                TransactionEntity tx = TransactionMapper.mapResultSetToEntity(rs);
                list.add(tx);
            }
        }
        return list;
    }

    // update status of a transaction
    public void updateStatus(String transactionId, String status) throws SQLException {
        String sql = "UPDATE transactions SET status = '" + status + "' WHERE transaction_id = '" + transactionId + "'";
        dm.query(sql);
    }

}
