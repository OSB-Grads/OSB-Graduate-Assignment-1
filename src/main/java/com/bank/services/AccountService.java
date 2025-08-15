package com.bank.services;

import com.bank.db.AccountDAO;
import com.bank.db.LogDAO;
import com.bank.dto.AccountDTO;
import com.bank.dto.UserDTO;
import com.bank.entity.AccountEntity;
import com.bank.entity.UserEntity;
import com.bank.mapper.AccountMapper;
import com.bank.util.ConsoleColor;

import java.security.PublicKey;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
        String now = (new Timestamp(System.currentTimeMillis())).toString();
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
                System.out.println(ConsoleColor.GREEN+"Account created successfully! Account Number: " + accountNumber+ConsoleColor.RESET);

            } catch (RuntimeException e) {
                if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("constraint")) {
                    System.out.println(ConsoleColor.RED+"Duplicate account number detected, regenerating... (Attempt " + attempts + ")"+ConsoleColor.RESET);
                } else {
                    throw e;
                }
            }
        }

        if (!created) {
            throw new RuntimeException(ConsoleColor.YELLOW+"Unable to create account after " + attempts + " attempts."+ConsoleColor.RESET);
        }
    }

    private String generateUniqueAccountNumberUUID() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 10);
    }
    public List<AccountDTO> getAccountsByUserId(int userid){

        List<AccountDTO>accounts=new ArrayList<>();
        List<AccountEntity> acc=accountDAO.getAccountsByUserId(userid);

        for(AccountEntity entity:acc){
            accounts.add(AccountMapper.entityToDTO(entity));
        }
        return accounts;

    }

}
