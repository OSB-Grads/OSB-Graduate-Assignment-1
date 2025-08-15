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
import com.bank.util.ConsoleColor;

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
    private String selectAccountNumber(int userId, boolean credit) throws AccountNotFoundException {
        List<AccountEntity> listOfUsers = accountDAO.getAccountsByUserId(userId);
        if (listOfUsers.isEmpty()) {
            System.out.println(ConsoleColor.RED+"No Accounts found for this user."+ConsoleColor.RESET);
            return null;
        }

        System.out.println(ConsoleColor.BLUE+"Available Accounts"+ConsoleColor.RESET);
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (credit || !listOfUsers.get(i).isIs_locked())
                System.out.println((i + 1) + ". Account Number: " + listOfUsers.get(i).getAccount_number());
        }

        System.out.println(ConsoleColor.BLUE+"Choose account number (1 to " + listOfUsers.size() + "):"+ConsoleColor.RESET);
        int index = -1;
        while (index < 0 || index >= listOfUsers.size()) {
            index = sc.nextInt() - 1;
            if (index < 0 || index >= listOfUsers.size()) {
                System.out.println("Invalid choice :( Try Again");
            }
        }
        return listOfUsers.get(index).getAccount_number();
    }


    public boolean transactAmountBetweenUsers(int userId) throws AccountNotFoundException,BankingException, SQLException {
        System.out.println(ConsoleColor.BLUE+"Select From Account Number"+ConsoleColor.RESET);
        String fromAccountNumber = selectAccountNumber(userId, false);
        if(fromAccountNumber==null)return false;
        System.out.println(ConsoleColor.BLUE+"Enter To Account Number "+ConsoleColor.RESET);
        String ToAccountNumber = sc.next();

        AccountEntity accountEntity = accountDAO.getAccountById(ToAccountNumber);

        if (accountEntity == null) {
            throw new AccountNotFoundException("To Account is not available" + ToAccountNumber);
        }
        System.out.println(ConsoleColor.BLUE+"Enter the Amount ::"+ConsoleColor.RESET);
        double amount = sc.nextDouble();
        try {
            TransactionEntity dbT = transactionService.debitFromAccount(fromAccountNumber, amount);
            TransactionEntity crT = transactionService.creditToAccount(ToAccountNumber, amount);
            crT.setFrom_account_id(fromAccountNumber);
            transactionDAO.saveTransaction(crT);
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Successful","user_ip",LogDAO.Status.SUCCESS);
            return true;
        } catch (DebitFailureException e) {
            System.out.println(ConsoleColor.RED+"Debit Operation has failed"+ConsoleColor.RESET);
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            throw new BankingException("Debit Operation has failed");

        } catch ( CreditFailureException e) {
            System.out.println(ConsoleColor.RED+"Credit Operation has failed"+ConsoleColor.RESET);
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            transactionService.creditToAccount(fromAccountNumber, amount);
        } catch (TransactionFailureException e) {
            System.out.println(ConsoleColor.RED+"Credit Operation has failed"+ConsoleColor.RESET);
            LogService.logintoDB(userId, LogDAO.Action.TRANSACTIONS,"Transaction Failure","user_ip",LogDAO.Status.FAILURE);
            transactionService.creditToAccount(fromAccountNumber, amount);
            transactionService.debitFromAccount(ToAccountNumber, amount);
        }
        return false;
    }
}
