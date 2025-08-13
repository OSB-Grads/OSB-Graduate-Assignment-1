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


    public void handle(Long userId) throws AccountNotFoundException {
        List<AccountEntity> listOfUsers = accountDAO.getAccountsByUserId(userId);
        if (listOfUsers.isEmpty()) {
            throw new AccountNotFoundException("No Accounts found for user : " + userId);
        }

        try {
            System.out.println("Available Accounts");
            for (int i = 0; i < listOfUsers.size(); i++) {
                System.out.println((i + 1) + ". Account Number" + listOfUsers.get(i).getAccount_number());
            }
            System.out.println("Choose account number (1 to " + listOfUsers.size() + "):");
            int index = -1;
            while (index < 0 || index >= listOfUsers.size()) {
                index = sc.nextInt() - 1;
                if (index < 0 || index >= listOfUsers.size()) {
                    System.out.println("Invalid choice :( Try Again  ");
                }
            }
            String accountNumber = listOfUsers.get(index).getAccount_number();

            System.out.print("Enter Amount to deposit :");
            Double depositAmount = sc.nextDouble();

            TransactionEntity transaction = transactionService.creditToAccount(accountNumber, depositAmount);
            transactionDAO.saveTransaction(transaction);
            System.out.println("Deposit successful for account:" + accountNumber);

        } catch (BankingException | SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }
}
