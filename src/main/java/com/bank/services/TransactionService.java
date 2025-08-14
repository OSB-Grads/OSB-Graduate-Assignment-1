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

import java.sql.Timestamp;


public class TransactionService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public TransactionEntity creditToAccount(String accountNumber, double amount) throws AccountNotFoundException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }

        AccountEntity account = accountDAO.getAccountById(accountNumber);
        if (account == null) {
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS,"Account is not available with bank","USER IP",LogDAO.Status.FAILURE);
            throw new AccountNotFoundException(accountNumber);
        }

        AccountDTO accountDTO = AccountMapper.entityToDTO(account);
        double newBalance = accountDTO.getBalance() + amount;
        int user_id=accountDTO.getUserId();

        accountDTO.setBalance(newBalance);
        AccountEntity updatedAccount=AccountMapper.dtoToEntity(accountDTO);
        accountDAO.updateAccountDetails(updatedAccount);
        LogService.logintoDB( user_id, LogDAO.Action.TRANSACTIONS,"Amount has been credited into User Account","USER IP",LogDAO.Status.SUCCESS);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setFrom_account_id(null); // Since it's a deposit
        transaction.setTo_account_id(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionDAO.TransactionType.DEPOSIT.name()); // Convert enum to String
        transaction.setDescription("Deposit of " + amount);
        transaction.setStatus(TransactionDAO.Status.SUCCESS.name()); // Convert enum to String
        transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return transaction;
    }

    public TransactionEntity debitFromAccount(String accountNumber,double debitAmount) throws BankingException {

        AccountEntity accountEntity=accountDAO.getAccountById(accountNumber);
        if(accountEntity==null){
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS,"Account is not available with bank","USER IP",LogDAO.Status.FAILURE);
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

        TransactionEntity transaction = new TransactionEntity();
        transaction.setFrom_account_id(accountNumber); // Since money is debited from this account
        transaction.setTo_account_id(null); // No target account for direct withdrawal
        transaction.setAmount(debitAmount);
        transaction.setTransaction_type(TransactionDAO.TransactionType.WITHDRAW.name()); // Enum to String
        transaction.setDescription("Withdrawal of ₹" + debitAmount);
        transaction.setStatus(TransactionDAO.Status.SUCCESS.name()); // Enum to String
        transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return transaction;
    }

}
