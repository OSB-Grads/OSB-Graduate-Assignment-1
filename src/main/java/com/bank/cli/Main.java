package com.bank.cli;

import com.bank.Orchestrators.DepositAndWithdrawOrchestrator;
import com.bank.Orchestrators.TransactOrchestrator;
import com.bank.Orchestrators.UserOrchestrator;
import com.bank.db.AccountDAO;
import com.bank.db.DatabaseManager;
import com.bank.cli.display.MenuDisplay;
import com.bank.mapper.AccountMapper;
import com.bank.mapper.UserMapper;
import com.bank.services.AccountService;
import com.bank.services.AuthService;
import com.bank.services.LogService;
import com.bank.db.userDao;
import com.bank.services.UserService;
import com.bank.util.PasswordUtil;
import com.bank.services.LogService;

/**
 * Main entry point for the CLI Banking Application.
 * This class initializes the application and starts the main menu loop.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to CLI Banking Application ===");
        System.out.println("Initializing application...");
        
        try {
            // Initialize database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            UserService userService = new UserService(dbManager);



            if (!dbManager.isConnected()) {
                System.err.println("Failed to connect to database. Exiting...");
                System.exit(1);
            }

            AccountDAO accountDAO = new AccountDAO(dbManager);          // DAO needs DB Manager
            AccountMapper accountMapper = new AccountMapper();          // Mapper has no dependencies
            LogService logService = new LogService();          // LogService also needs DB Manager
            AccountService accountService = new AccountService(
                    accountDAO,
                    accountMapper,
                    logService
            );


            userDao userDao = new userDao(dbManager);
            PasswordUtil passwordUtil = new PasswordUtil();
            UserMapper userMapper = new UserMapper();

           AuthService authService = new AuthService(
                    userDao,
                    passwordUtil,
                    userMapper


            );
            UserOrchestrator userOrchestrator = new UserOrchestrator(userService, authService);
            DepositAndWithdrawOrchestrator depositAndWithdrawOrchestrator=new DepositAndWithdrawOrchestrator();
            TransactOrchestrator transactOrchestrator=new TransactOrchestrator();
            System.out.println("Application initialized successfully!");
            System.out.println("==========================================");

            // Start the main menu

            MenuDisplay menuDisplay = new MenuDisplay(accountService, authService, userOrchestrator,depositAndWithdrawOrchestrator,transactOrchestrator);
            menuDisplay.showMainMenu();

        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            // Cleanup resources
            try {
                DatabaseManager.getInstance().close();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
