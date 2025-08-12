package com.bank.mapper;

import com.bank.entity.TransactionEntity;
import com.bank.dto.TransactionDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionMapper {
    public static TransactionEntity mapToTransactionEntity(Map<String, Object>row){
        if(row==null || row.isEmpty()) {
            return null;
        }
        TransactionEntity transaction =new TransactionEntity();
        transaction.setTransaction_id((int) row.get("id"));
        transaction.setFrom_account_id((int) row.get("from_account_id"));
        transaction.setTo_account_id((int) row.get("to_account_id"));
        transaction.setTransaction_type((String) row.get("transaction_type"));
        transaction.setAmount((double) row.get("amount"));
        transaction.setStatus((String) row.get("status"));
        transaction.setDescription((String) row.get("description"));
        transaction.setCreatedAt((Timestamp) row.get("created_at"));
        return transaction;
    }
    public static TransactionDTO  transactionEntityToDto(TransactionEntity transaction) {

        if (transaction == null) {
            return null;
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransaction_id((int) transaction.getTransaction_id());
        transactionDTO.setFrom_account_id((int) transaction.getFrom_account_id());
        transactionDTO.setTo_account_id((int) transaction.getTo_account_id());
        transactionDTO.setTransaction_type((String) transaction.getTransaction_type());
        transactionDTO.setAmount((double) transaction.getAmount());
        transactionDTO.setStatus((String) transaction.getStatus());
        transactionDTO.setDescription((String) transaction.getDescription());
        transactionDTO.setCreated_at((Timestamp) transaction.getCreatedAt());
        return transactionDTO;
    }

    public static TransactionEntity transactionDtoToEntity(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            return null;
        }
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransaction_id(transactionDTO.getTransaction_id());
        transaction.setFrom_account_id(transactionDTO.getFrom_account_id());
        transaction.setTo_account_id(transactionDTO.getTo_account_id());
        transaction.setTransaction_type(transactionDTO.getTransaction_type());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setCreatedAt(transactionDTO.getCreated_at());

        return transaction;
    }

    public static List<TransactionEntity> mapToTransactionEntityList(List<Map<String, Object>> rows) {
        if (rows == null) {
            return new ArrayList<>();
        }
        return rows.stream()
                .map(TransactionMapper::mapToTransactionEntity)
                .collect(Collectors.toList());
    }

}
