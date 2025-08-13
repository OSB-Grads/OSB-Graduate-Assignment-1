package com.bank.Orchestrators;

import com.bank.db.AccountDAO;
import com.bank.db.DatabaseManager;
import com.bank.db.TransactionDAO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.services.TransactionService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DepositAndWithdrawOrchestrator {
    private final Scanner sc = new Scanner(System.in);
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    private final AccountDAO accountDAO = new AccountDAO(dbManager);
    private final TransactionDAO transactionDAO = new TransactionDAO(dbManager);
    private final TransactionService transactionService = new TransactionService(accountDAO, transactionDAO);

    /**
     * Common method to display accounts for a user and get the selected account number.
     */
    private String selectAccountNumber(Long userId) throws AccountNotFoundException {
        List<AccountEntity> listOfUsers = accountDAO.getAccountsByUserId(userId);
        if (listOfUsers.isEmpty()) {
            throw new AccountNotFoundException("No Accounts found for user : " + userId);
        }

        System.out.println("Available Accounts");
        for (int i = 0; i < listOfUsers.size(); i++) {
            if(!listOfUsers.get(i).isIs_locked())
            System.out.println((i + 1) + ". Account Number: " + listOfUsers.get(i).getAccount_number());
        }

        System.out.println("Choose account number (1 to " + listOfUsers.size() + "):");
        int index = -1;
        while (index < 0 || index >= listOfUsers.size()) {
            index = sc.nextInt() - 1;
            if (index < 0 || index >= listOfUsers.size()) {
                System.out.println("Invalid choice :( Try Again");
            }
        }
        return listOfUsers.get(index).getAccount_number();
    }

    public void handleDeposit(Long userId) throws AccountNotFoundException {
        try {
            String accountNumber = selectAccountNumber(userId);

            System.out.print("Enter Amount to Deposit: ");
            double depositAmount = sc.nextDouble();

            TransactionEntity transaction = transactionService.creditToAccount(accountNumber, depositAmount);
            transactionDAO.saveTransaction(transaction);

            System.out.println("Deposit successful for account: " + accountNumber);
        } catch (BankingException | SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    public void handleWithdraw(Long userId) throws AccountNotFoundException {
        try {
            String accountNumber = selectAccountNumber(userId);

            System.out.print("Enter Amount to Withdraw: ");
            double withdrawAmount = sc.nextDouble();

            TransactionEntity transaction = transactionService.debitFromAccount(accountNumber, withdrawAmount);
            transactionDAO.saveTransaction(transaction);

            System.out.println("Withdrawal of " + withdrawAmount + " is successful from: " + accountNumber);
        } catch (BankingException | SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }
}
