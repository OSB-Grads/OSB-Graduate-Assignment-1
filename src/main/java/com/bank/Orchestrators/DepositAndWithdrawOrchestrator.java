package com.bank.Orchestrators;

import com.bank.db.AccountDAO;
import com.bank.db.DatabaseManager;
import com.bank.db.TransactionDAO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.services.TransactionService;
import com.bank.util.ConsoleColor;

import java.sql.SQLException;
import java.util.Comparator;
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
    private String selectAccountNumber(int userId,boolean credit) throws AccountNotFoundException {
        List<AccountEntity> listOfAccounts = accountDAO.getAccountsByUserId(userId);
        if (listOfAccounts.isEmpty()) {
            System.out.println(ConsoleColor.YELLOW+"No Accounts found for this user." +ConsoleColor.RESET);
            return null;
        }

        System.out.println(ConsoleColor.BLUE+"Available Accounts"+ConsoleColor.RESET);
        for (int i = 0; i < listOfAccounts.size(); i++) {
            if(credit || !listOfAccounts.get(i).isIs_locked())
            System.out.println((i + 1) + ". Account Number: " + listOfAccounts.get(i).getAccount_number());
        }

        System.out.println(ConsoleColor.BLUE+"Choose account number (1 to " + listOfAccounts.size() + "):"+ConsoleColor.RESET);
        int index = -1;
        while (index < 0 || index >= listOfAccounts.size()) {
            index = sc.nextInt() - 1;
            if (index < 0 || index >= listOfAccounts.size()) {
                System.out.println("Invalid choice :( Try Again");
            }
        }
        return listOfAccounts.get(index).getAccount_number();
    }

    public void handleDeposit(int userId) throws AccountNotFoundException, SQLException {
     //   try {
            String accountNumber = selectAccountNumber(userId,true);
            if (accountNumber == null || accountNumber.isEmpty()){
                System.out.println(ConsoleColor.YELLOW+"Please create Account to Deposit amount"+ConsoleColor.RESET);
                return ;
            }

            System.out.print(ConsoleColor.BLUE+"Enter Amount to Deposit: "+ConsoleColor.RESET);
            double depositAmount = sc.nextDouble();

            TransactionEntity transaction = transactionService.creditToAccount(accountNumber, depositAmount);
            transactionDAO.saveTransaction(transaction);

            System.out.println(ConsoleColor.GREEN+"Deposit successful for account: " + accountNumber+ConsoleColor.RESET);
    }

    public void handleWithdraw(int userId) throws BankingException, SQLException {
       // try {
            String accountNumber = selectAccountNumber(userId,false);
            if (accountNumber == null || accountNumber.isEmpty()){
                System.out.println(ConsoleColor.YELLOW+"Please create Account to Withdraw amount"+ConsoleColor.RESET);
                return;
            }

            System.out.print(ConsoleColor.BLUE+"Enter Amount to Withdraw: "+ConsoleColor.RESET);
            double withdrawAmount = sc.nextDouble();

            TransactionEntity transaction = transactionService.debitFromAccount(accountNumber, withdrawAmount);
            transactionDAO.saveTransaction(transaction);

            System.out.println(ConsoleColor.GREEN+"Withdrawal of " + withdrawAmount + " is successful from: " + accountNumber+ConsoleColor.RESET);
    }
}
