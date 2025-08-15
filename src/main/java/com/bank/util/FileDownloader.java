package com.bank.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import com.bank.dto.TransactionDTO;
import com.bank.services.TransactionService;

public class FileDownloader {

    public static void downloadAsCSV(List<TransactionDTO> listoftransactions) {
        String fileName = "transactions.csv";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.append("Transaction ID, Type, Amount, Date, From Account, To Account, Status\n");
            for (TransactionDTO t : listoftransactions) {
                fileWriter.append(t.getTransaction_id() + ",")
                        .append(t.getTransaction_type() + ",")
                        .append(t.getAmount() + ",")
                        .append(t.getCreated_at() + ",")
                        .append(t.getFrom_account_id() + ",")
                        .append(t.getTo_account_id() + ",")
                        .append(t.getStatus() + "\n");
            }
            System.out
                    .println(ConsoleColor.GREEN + "CSV file downloaded Successfully : " + ConsoleColor.YELLOW + fileName
                            + ConsoleColor.RESET);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void downloadASTXT(List<TransactionDTO> listoftransactions) {
        String fileName = "transactions.txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (TransactionDTO t : listoftransactions) {
                fileWriter.append("ID : " + t.getTransaction_id() + "\n")
                        .append("Type :" + t.getTransaction_type() + "\n")
                        .append("Amount : " + t.getAmount() + "\n")
                        .append("Date : " + t.getCreated_at() + "\n")
                        .append("From Account : " + t.getFrom_account_id() + "\n")
                        .append("To Account : " + t.getTo_account_id() + "\n")
                        .append("Status : " + t.getStatus() + "\n")
                        .append("---------------------------" + "\n");
            }
            System.out.println(ConsoleColor.GREEN + "TXT file downloaded successfully : " + ConsoleColor.YELLOW
                    + fileName + ConsoleColor.RESET);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
