package com.bank.services;

import com.bank.db.AccountDAO;
import com.bank.db.LogDAO;
import com.bank.dto.AccountDTO;
import com.bank.entity.AccountEntity;
import com.bank.mapper.AccountMapper;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;


public class AccountService {
    private final AccountDAO accountDAO;
    private final AccountMapper accountMapper;
    private final LogService logService;

    public AccountService(AccountDAO accountDAO,AccountMapper accountMapper,LogService logService) {
        this.accountDAO = accountDAO;
        this.accountMapper=accountMapper;
        this.logService=logService;
    }
    public void createAccount(AccountDTO accountDTO) throws SQLException {
        boolean created = false;
        int attempts = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        accountDTO.setCreatedAt(now);
        accountDTO.setUpdatedAt(now);

        while (!created && attempts < 5) {
            attempts++;
            String accountNumber = generateUniqueAccountNumberUUID();
            accountDTO.setAccountNumber(accountNumber);
            AccountEntity accountEntity = accountMapper.dtoToEntity(accountDTO);

            try {

                accountDAO.insertAccountDetails(accountEntity);
                logService.logintoDB(
                        accountDTO.getUserId(),
                        LogDAO.Action.CREATION_MANAGEMENT,
                        "Account created successfully",
                        "Account number: " + accountNumber,
                        LogDAO.Status.SUCCESS
                );
                created = true;
                System.out.println("Account created successfully! Account Number: " + accountNumber);

            } catch (RuntimeException e) {
                if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("constraint")) {
                    System.out.println("Duplicate account number detected, regenerating... (Attempt " + attempts + ")");
                } else {
                    throw e;
                }
            }
        }

        if (!created) {
            throw new RuntimeException("Unable to create account after " + attempts + " attempts.");
        }
    }

    private String generateUniqueAccountNumberUUID() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 10);
    }

}
