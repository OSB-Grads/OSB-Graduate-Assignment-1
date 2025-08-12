package com.bank.services;

import com.bank.db.TransactionDAO;
import com.bank.entity.AccountEntity;
import com.bank.entity.TransactionEntity;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.db.AccountDAO;


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
            throw new AccountNotFoundException(accountNumber);
        }
// TODO : to be edited post creation of FD funding EndDate
//        if("Fixed Deposit".equalsIgnoreCase(account.getAccount_type())){
//            if (account.getFundingEndDate()!= null&& LocalDateTime.now().isAfter(account.getFundingEndDate())) {
//                throw new BankingException("Cannot credit. FD funding period is over.");
//        }
//    }
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountDAO.updateAccountDetails(account);

        //create transaction record
        TransactionEntity transaction = new TransactionEntity();
        transactionDAO.saveTransaction(transaction);
    }

}
