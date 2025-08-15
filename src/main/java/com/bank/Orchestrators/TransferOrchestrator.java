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

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TransferOrchestrator {

    private final Scanner sc = new Scanner(System.in);
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    private final AccountDAO accountDAO = new AccountDAO(dbManager);
    private final TransactionDAO transactionDAO = new TransactionDAO(dbManager);
    private final TransactionService transactionService = new TransactionService(accountDAO, transactionDAO);

    public TransferOrchestrator() {
    }

    public boolean transfer(int userId) throws NullPointerException,BankingException, SQLException {
        // get account details
        System.out.println("==== FROM ACCOUNT ====");
        String fromAccountNumber = selectAccountNumber(userId,false);
        System.out.println("==== TO ACCOUNT ====");
        String toAccountNumber = selectAccountNumber(userId,true);
        if (fromAccountNumber.equals(toAccountNumber)) {
            System.out.println(ConsoleColor.YELLOW+"Cannot transfer to the same account."+ConsoleColor.RESET);
            return false;
        }

        System.out.print(ConsoleColor.BLUE+"Enter amount to transfer: "+ConsoleColor.RESET);
        double amount = sc.nextDouble();
        if (amount <= 0) {
            System.out.println(ConsoleColor.YELLOW+ "Invalid amount. Must be positive."+ConsoleColor.RESET);
            return false;
        }
        try {
            TransactionEntity dbT = transactionService.debitFromAccount(fromAccountNumber, amount);
            TransactionEntity crT = transactionService.creditToAccount(toAccountNumber, amount);
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
            transactionService.debitFromAccount(toAccountNumber, amount);
        }
        return false;

    }


    private String selectAccountNumber(int userId,boolean credit) throws AccountNotFoundException {
        List<AccountEntity> listOfUsers = accountDAO.getAccountsByUserId(userId);
        if (listOfUsers.isEmpty()) {
            System.out.println(ConsoleColor.YELLOW+"No Accounts found for this user." +ConsoleColor.RESET );
            return null;
        }

        System.out.println(ConsoleColor.BLUE+"Available Accounts"+ConsoleColor.RESET);
        for (int i = 0; i < listOfUsers.size(); i++) {
            if(credit || !listOfUsers.get(i).isIs_locked())
                System.out.println((i + 1) + ". Account Number: " + listOfUsers.get(i).getAccount_number());
        }

        System.out.println(ConsoleColor.BLUE+"Choose account number (1 to " + listOfUsers.size() + "):"+ConsoleColor.RESET);
        int index = -1;
        while (index < 0 || index >= listOfUsers.size()) {
            index = sc.nextInt() - 1;
            if (index < 0 || index >= listOfUsers.size()) {
                System.out.println(ConsoleColor.YELLOW+ "Invalid choice :( Try Again"+ ConsoleColor.RESET);
            }
        }
        return listOfUsers.get(index).getAccount_number();
    }
}
