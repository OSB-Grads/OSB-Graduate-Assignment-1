package com.bank.mapper;

import com.bank.dto.AccountDTO;
import com.bank.entity.AccountEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountMapper {



    public static AccountEntity mapToAccountEntity(Map<String, Object> row) {
        if (row == null || row.isEmpty()) return null;

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUser_id((int) row.get("user_id"));
        accountEntity.setAccount_number((String) row.get("account_number"));
        accountEntity.setAccount_created((Timestamp) row.get("account_created"));
        accountEntity.setAccount_updated((Timestamp) row.get("account_updated"));
        accountEntity.setAccount_type((String) row.get("account_type"));
        accountEntity.setIs_locked((boolean) row.get("is_locked"));
        accountEntity.setBalance((double) row.get("balance"));

        String type = (String) row.get("account_type");
        double interest = "FIXED_DEPOSIT".equalsIgnoreCase(type) ? 0.05 : 0.03;
        accountEntity.setInterest(interest);

        return accountEntity;
    }

    public static List<AccountEntity> mapToAccountEntityList(List<Map<String, Object>> rows) {
        if (rows == null) {
            return new ArrayList<>();
        }
        return rows.stream()
                .map(AccountMapper::mapToAccountEntity)
                .collect(Collectors.toList());
    }

    public static AccountEntity dtoToEntity(AccountDTO accountDTO) {
        if (accountDTO == null) return null;

        double interest = "FIXED_DEPOSIT".equalsIgnoreCase(accountDTO.getAccountType()) ? 0.05 : 0.03;

        return new AccountEntity(

                accountDTO.getAccountNumber(),
                accountDTO.getUserId(),
                accountDTO.getAccountType(),
                accountDTO.getBalance(),
                accountDTO.isLocked(),
                accountDTO.getCreatedAt(),
                accountDTO.getUpdatedAt(),
                accountDTO.getAccountType(),
                interest
        );
    }

    public static AccountDTO entityToDTO(AccountEntity accountEntity) {
        if (accountEntity == null) return null;

        return new AccountDTO(
                accountEntity.getAccount_number(),
                accountEntity.getUser_id(),
                accountEntity.getAccount_type(),
                accountEntity.getBalance(),
                accountEntity.isIs_locked(),
                accountEntity.getAccount_created(),
                accountEntity.getAccount_updated()
        );
    }
}
