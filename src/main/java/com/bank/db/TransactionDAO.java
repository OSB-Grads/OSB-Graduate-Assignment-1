package com.bank.db;
import com.bank.entity.TransactionEntity;
import com.bank.mapper.TransactionMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionDAO {
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }
    public enum Status{
        COMPLETED, FAILED, PENDING
    }

    private final DatabaseManager dm;
    public TransactionDAO(DatabaseManager dm) {
        this.dm = dm;
    }

    //get all transactions
    public List<TransactionEntity> getAllAccounts() throws SQLException {
        List<TransactionEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        List<Map<String, Object>> rows = dm.query(sql);
        return TransactionMapper.mapToTransactionEntityList(rows);
    }


    // get transaction by Id
//    public TransactionEntity getTransaction(String accountId) throws SQLException {
//        String sql = "SELECT * FROM transactions WHERE transaction_id = '" + accountId + "'";
//        List<Map<String, Object>> rows = dm.query(sql);
//
//        if (!rows.isEmpty()) {
//            return TransactionMapper.mapToTransactionEntity(rows.get(0));
//        }
//        return null;
//    }

    //get all transactions by Account Id
    public List<TransactionEntity> getTransactionsByAccountNumber(String account_number) throws SQLException {
        List<TransactionEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account_id = " + account_number + " OR to_account_id = " + account_number;
        List<Map<String, Object>> rows = dm.query(sql);
        return TransactionMapper.mapToTransactionEntityList(rows);
    }

    public void saveTransaction( TransactionEntity transaction) throws SQLException{
        String sql = String.format(
                "INSERT INTO transactions (transaction_id, from_account_id, to_account_id, transaction_type, amount, description, status) " +
                        "VALUES (%s, %s, %s, '%s', %f, '%s', '%s')",
                transaction.getTransaction_id(),
                transaction.getFrom_account_id(),
                transaction.getTo_account_id(),
                transaction.getTransaction_type(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getStatus()
//                transaction.getCreatedAt().toString()
        );
        dm.query(sql);
    }


    // update status of a transaction
    public void updateStatus(String transactionId, String status) throws SQLException {
        String sql = "UPDATE transactions SET status = '" + status + "' WHERE transaction_id = '" + transactionId + "'";
        dm.query(sql);
    }

}
