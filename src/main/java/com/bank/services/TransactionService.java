package com.bank.services;

import com.bank.db.LogDAO;
import com.bank.db.TransactionDAO;
import com.bank.dto.AccountDTO;
import com.bank.dto.TransactionDTO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.db.AccountDAO;
import com.bank.exception.InsufficientFundsException;
import com.bank.mapper.AccountMapper;
import com.bank.mapper.TransactionMapper;
import com.bank.util.ConsoleColor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionService {

    private static AccountDAO accountDAO;
    private static TransactionDAO transactionDAO;

    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public TransactionService() {
    }

    private String generateUniqueAccountNumberUUID() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 10);
    }

    public TransactionEntity creditToAccount(String accountNumber, double amount) throws AccountNotFoundException {
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return null;
        }

        AccountEntity account = accountDAO.getAccountById(accountNumber);
        if (account == null) {
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS, "Account is not available with bank", "USER IP", LogDAO.Status.FAILURE);
            throw new AccountNotFoundException(accountNumber);
        }

        AccountDTO accountDTO = AccountMapper.entityToDTO(account);
        double newBalance = accountDTO.getBalance() + amount;
        int user_id = accountDTO.getUserId();

        accountDTO.setBalance(newBalance);
        AccountEntity updatedAccount = AccountMapper.dtoToEntity(accountDTO);
        accountDAO.updateAccountDetails(updatedAccount);
        LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS, "Amount has been credited into User Account", "USER IP", LogDAO.Status.SUCCESS);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransaction_id(generateUniqueAccountNumberUUID());
        transaction.setFrom_account_id(null); // Since it's a deposit
        transaction.setTo_account_id(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionDAO.TransactionType.DEPOSIT.name()); // Convert enum to String
        transaction.setDescription("Deposit of " + amount);
        transaction.setStatus(TransactionDAO.Status.COMPLETED.name()); // Convert enum to String
//        transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());

        return transaction;
    }

    public TransactionEntity debitFromAccount(String accountNumber, double debitAmount) throws BankingException {

        AccountEntity accountEntity = accountDAO.getAccountById(accountNumber);
        if (accountEntity == null) {
            LogService.logintoDB(-1, LogDAO.Action.TRANSACTIONS, "Account is not available with bank", "USER IP", LogDAO.Status.FAILURE);
            throw new AccountNotFoundException(accountNumber);
        }
        AccountDTO accountDTO = AccountMapper.entityToDTO(accountEntity);
        double balance = accountDTO.getBalance();
        int user_id = accountDTO.getUserId();

        if (debitAmount <= 0) {
            System.out.println(ConsoleColor.YELLOW + "Please enter a valid amount to perform debit operation" + ConsoleColor.RESET);
            return null;
        } else if (balance < debitAmount) {
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS, "Sufficient balance is not available in the account", "USER IP", LogDAO.Status.FAILURE);
            throw new InsufficientFundsException(debitAmount + "Not available in the Account");
        } else if (!accountDTO.isLocked()) {
            accountDTO.setBalance(balance - debitAmount);
            accountEntity = AccountMapper.dtoToEntity(accountDTO);
            accountDAO.updateAccountDetails(accountEntity);
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS, "Amount has been debited from user account", "USER IP", LogDAO.Status.SUCCESS);
        } else {
            LogService.logintoDB(user_id, LogDAO.Action.TRANSACTIONS, "Cannot debit from FD(Fixed Deposit)", "USER IP", LogDAO.Status.FAILURE);
            throw new BankingException(ConsoleColor.YELLOW + "Cannot debit from FD(Fixed Deposit)" + ConsoleColor.RESET);
        }

        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransaction_id(generateUniqueAccountNumberUUID());
        transaction.setFrom_account_id(accountNumber); // Since money is debited from this account
        transaction.setTo_account_id(null); // No target account for direct withdrawal
        transaction.setAmount(debitAmount);
        transaction.setTransaction_type(TransactionDAO.TransactionType.WITHDRAWAL.name()); // Enum to String
        transaction.setDescription("Withdrawal of ₹" + debitAmount);
        transaction.setStatus(TransactionDAO.Status.COMPLETED.name()); // Enum to String
        // transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());

        return transaction;
    }

    public List<TransactionDTO> getTransactionHistoryById(int user_id) throws BankingException, SQLException {
        List<TransactionEntity> resultTransaction = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        // get all accounts by user_id
        List<AccountEntity> accounts = accountDAO.getAccountsByUserId(user_id);
        if (accounts.isEmpty() || accounts.size() == 0) {
            System.out.println(ConsoleColor.YELLOW + "There exists no accounts for this UserID." + ConsoleColor.RESET);
            return null;
        }

        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccount_number());
        }
        System.out.println("Choose account number (1 to " + accounts.size() + "):");
        int index = -1;
        while (index < 0 || index >= accounts.size()) {
            index = sc.nextInt() - 1;
            if (index < 0 || index >= accounts.size()) {
                System.out.println(ConsoleColor.YELLOW + "Invalid choice :( Try Again" + ConsoleColor.RESET);
            }
        }
        String TransactionAccountNumber = accounts.get(index).getAccount_number();
        resultTransaction = transactionDAO.getTransactionsByAccountNumber(TransactionAccountNumber);
        return TransactionMapper.entityToTransactionDtoList(resultTransaction);
    }

    public void getTransactionHistoryForUser(int UserId) throws BankingException, SQLException {

        Scanner sc = new Scanner(System.in);
        List<AccountEntity> accounts = accountDAO.getAccountsByUserId(UserId);
        if (accounts.isEmpty() || accounts.size() == 0) {
            System.out.println(ConsoleColor.YELLOW + "There exists no  accounts associated with this UserID." + ConsoleColor.RESET);
        }

        System.out.println(ConsoleColor.PURPLE + "==== Transaction History by Account Number =====" + ConsoleColor.RESET);
        for (int i = 0; i < accounts.size(); i++) {
            String accountNumber = accounts.get(i).getAccount_number();
            String accountType = accounts.get(i).getType();
            double balance = accounts.get(i).getBalance();

            System.out.println("Account Number : " + accountNumber);
            System.out.println("Account Type : " + accountType);

            List<TransactionEntity> eachAccountTransactions = transactionDAO.getTransactionsByAccountNumber(accountNumber);
            List<TransactionDTO> transactionDTOs = TransactionMapper.entityToTransactionDtoList(eachAccountTransactions);

            if (transactionDTOs == null || transactionDTOs.isEmpty()) {
                System.out.println(ConsoleColor.YELLOW + "There are no transactions for this Account." + ConsoleColor.RESET);
            } else {
                // Table Header
                System.out.println(ConsoleColor.GREEN + "Transaction List:");
                System.out.printf("%-15s %-12s %-12s %-20s %-15s %-15s %-10s%n",
                        "Transaction ID", "Type", "Amount", "Date", "From Account", "To Account", "Status");
                System.out.println(
                        "------------------------------------------------------------------------------------------------------------------");

                // Table Rows
                for (TransactionDTO t : transactionDTOs) {
                    System.out.printf("%-15s %-12s %-12.2f %-20s %-15s %-15s %-10s%n",
                            t.getTransaction_id(),
                            t.getTransaction_type(),
                            t.getAmount(),
                            t.getCreated_at(),
                            t.getFrom_account_id(),
                            t.getTo_account_id(),
                            t.getStatus());
                }
                System.out.println(ConsoleColor.RESET);
            }
            System.out.printf("Current Balance: ₹%.2f%n", balance);
            System.out.println("===========================================================");
        }
    }
}
