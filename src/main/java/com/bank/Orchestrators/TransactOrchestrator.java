package com.bank.Orchestrators;

import com.bank.db.AccountDAO;
import com.bank.db.DatabaseManager;
import com.bank.db.LogDAO;
import com.bank.db.TransactionDAO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.*;
import com.bank.services.LogService;
import com.bank.services.TransactionService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TransactOrchestrator {
    private final Scanner sc = new Scanner(System.in);
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    private final AccountDAO accountDAO = new AccountDAO(dbManager);
    private final TransactionDAO transactionDAO = new TransactionDAO(dbManager);
    private final TransactionService transactionService = new TransactionService(accountDAO, transactionDAO);
    private final Connection connection = dbManager.getConnection();

    /**
     * Common method to display accounts for a user and get the selected account number.
     */
    private String selectAccountNumber(Long userId, boolean credit) throws AccountNotFoundException {
        List<AccountEntity> listOfUsers = accountDAO.getAccountsByUserId(userId);
        if (listOfUsers.isEmpty()) {
            throw new AccountNotFoundException("No Accounts found for user : " + userId);
        }

        System.out.println("Available Accounts");
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (credit || !listOfUsers.get(i).isIs_locked())
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


    public void transactAmountBetweenUsers(Long userId) throws BankingException, SQLException {
        System.out.println("Select From Account Number");
        String fromAccountNumber = selectAccountNumber(userId, false);
        System.out.println("Enter To Account Number ");
        String ToAccountNumber = sc.next();

        AccountEntity accountEntity = accountDAO.getAccountById(ToAccountNumber);

        if (accountEntity == null) {
            throw new AccountNotFoundException("To Account is not available" + ToAccountNumber);
        }
        System.out.println("Enter the Amount ::");
        double amount = sc.nextDouble();
        try {
            TransactionEntity dbT = transactionService.debitFromAccount(fromAccountNumber, amount);
            TransactionEntity crT = transactionService.creditToAccount(ToAccountNumber, amount);
            crT.setFrom_account_id(fromAccountNumber);
            transactionDAO.saveTransaction(crT);
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Successful","user_ip",LogDAO.Status.SUCCESS);
        } catch (DebitFailureException e) {
            System.out.println("Debit Operation has failed");
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            throw new BankingException("Debit Operation has failed");

        } catch ( CreditFailureException e) {
            System.out.println("Credit Operation has failed");
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            transactionService.creditToAccount(fromAccountNumber, amount);
        } catch (TransactionFailureException e) {
            System.out.println("Credit Operation has failed");
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            transactionService.creditToAccount(fromAccountNumber, amount);
            transactionService.debitFromAccount(ToAccountNumber, amount);
        }
    }
}
