package com.bank.services;

import com.bank.db.LogDAO;
import com.bank.db.TransactionDAO;
import com.bank.dto.AccountDTO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.db.AccountDAO;
import com.bank.exception.InsufficientFundsException;
import com.bank.mapper.AccountMapper;
import com.bank.mapper.TransactionMapper;

import java.sql.SQLException;


public class TransactionService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public void creditToAccount(String accountNumber, double amount) throws AccountNotFoundException {
        if (amount <= 0) {
            try {
                throw new BankingException("Amount must be grater than 0.");
            } catch (BankingException e) {
                throw new RuntimeException(e);
            }
        }

        AccountEntity account = accountDAO.getAccountById(accountNumber);
        if (account == null) {
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS,"Sufficient balance is not available in the account","USER IP",LogDAO.Status.FAILURE);
            throw new AccountNotFoundException(accountNumber);
        }

        AccountDTO accountDTO=AccountMapper.entityToDTO(account);
        double newBalance = accountDTO.getBalance() + amount;
        int user_id=accountDTO.getUserId();
        accountDTO.setBalance(newBalance);
        account=AccountMapper.dtoToEntity(accountDTO);
        accountDAO.updateAccountDetails(account);
        LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS,"Amount has been debited from user account","USER IP",LogDAO.Status.SUCCESS);
    }

    public void debitFromAccount(String accountNumber,double debitAmount) throws BankingException {

        AccountEntity accountEntity=accountDAO.getAccountById(accountNumber);
        if(accountEntity==null){
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS,"Sufficient balance is not available in the account","USER IP",LogDAO.Status.FAILURE);
            throw new AccountNotFoundException(accountNumber);
        }
        AccountDTO accountDTO=AccountMapper.entityToDTO(accountEntity);
        double balance=accountDTO.getBalance();
        int user_id=accountDTO.getUserId();

        if(balance<debitAmount){
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS,"Sufficient balance is not available in the account","USER IP",LogDAO.Status.FAILURE);
            throw new InsufficientFundsException(debitAmount+"Not available in the Account");
        }
        else if(!accountDTO.isLocked()){
            accountDTO.setBalance(balance-debitAmount);
            accountEntity=AccountMapper.dtoToEntity(accountDTO);
            accountDAO.updateAccountDetails(accountEntity);
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS,"Amount has been debited from user account","USER IP",LogDAO.Status.SUCCESS);
        }
        else{
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS,"Cannot debit from FD(Fixed Deposit)","USER IP",LogDAO.Status.FAILURE);
            throw new BankingException("Cannot debit from FD(Fixed Deposit)");
        }
    }

}
