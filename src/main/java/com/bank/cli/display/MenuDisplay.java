package com.bank.cli.display;

import com.bank.Orchestrators.DepositAndWithdrawOrchestrator;
import com.bank.Orchestrators.TransactOrchestrator;
import com.bank.Orchestrators.UserOrchestrator;
import com.bank.dto.AccountDTO;
import com.bank.dto.TransactionDTO;
import com.bank.dto.UserDTO;
import com.bank.exception.*;
import com.bank.services.AccountService;
import com.bank.services.AuthService;
import com.bank.services.TransactionService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;



/**
 * Handles all CLI menu display and user input.
 * This class is responsible for showing menus and collecting user choices.
 */
public class MenuDisplay {

    private int  UserId;
    private String currentUsername;
    private Scanner scanner;
    private final AccountService accountService;
    private final AuthService authService;
    private final UserOrchestrator userOrchestrator;
    private final DepositAndWithdrawOrchestrator depositAndWithdrawOrchestrator;
    private final TransactOrchestrator transactOrchestrator;

    public MenuDisplay(AccountService accountService, AuthService authService, UserOrchestrator userOrchestrator, DepositAndWithdrawOrchestrator depositAndWithdrawOrchestrator, TransactOrchestrator transactOrchestrator) {
        this.scanner = new Scanner(System.in);
        this.accountService = accountService;
        this.authService = authService;

        this.userOrchestrator = userOrchestrator;
        this.depositAndWithdrawOrchestrator = depositAndWithdrawOrchestrator;
        this.transactOrchestrator = transactOrchestrator;

    }



    /**
     * Display the main menu and handle user navigation.
     */
    public void showMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Create Customer Profile");
            System.out.println("3. Exit");
            System.out.print("Please select an option (1-3): ");

            try {

                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleCreateProfile();
                        break;
                    case 3:
                        System.out.println("Thank you for using CLI Banking Application!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Display the user menu after successful login.
     */
    public void showUserMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Create Bank Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Account Details");
            System.out.println("6. View Transaction History");
            System.out.println("7. Update Profile Info");
            System.out.println("8. Logout");
            System.out.print("Please select an option (1-8): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        handleCreateAccount();
                        break;
                    case 2:
                        handleDeposit();
                        break;
                    case 3:
                        handleWithdraw();
                        break;
                    case 4:
                        handleTransfer();
                        break;
                    case 5:
                        handleViewAccounts();
                        break;
                    case 6:
                        handleViewTransactionHistory();
                        break;
                    case 7:
                        handleUpdateProfile();
                        break;
                    case 8:
                        System.out.println("Logging out...");
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void viewUserProfile()  {
        try {
            userOrchestrator.displayProfile(UserId);
        }
        catch(Exception e){
            showError(e.getMessage());
        }
    }


    // TODO: Implement these methods by calling appropriate services/orchestrators

    private void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        UserDTO DTO = null;
        try {
            DTO = authService.validateUserCredentials(username,password);
            UserId=DTO.getId();
            currentUsername=DTO.getUsername();
            showUserMenu();
        } catch (InvalidCredentialsException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Exception has occured during Login Operation..!"+e.getMessage());
        }

    }

    private void handleCreateProfile() {
        System.out.println("\n=== CREATE CUSTOMER PROFILE ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        try {
            userOrchestrator.signup(UserId, username, password, fullName, email, phone);  // <-- underlined change
            showSuccess("Profile created successfully!");
        } catch(UserAlreadyExist e) {
            showError(e.getMessage());
        }
        catch (Exception e) {
            showError("Failed to create profile: " + e.getMessage());
        }
    }


    private void handleCreateAccount() {
        System.out.println("\n=== CREATE BANK ACCOUNT ===");
        System.out.println("1. Savings Account");
        System.out.println("2. Fixed Deposit Account");
        System.out.print("Select account type (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            String accountType;
            boolean isLocked;

            switch (choice) {
                case 1:
                    accountType = "SAVINGS";
                    isLocked = false;
                    break;
                case 2:
                    accountType = "FIXED_DEPOSIT";
                    isLocked = true;
                    break;
                default:
                    System.out.println("Invalid account type selected.");
                    return;
            }

            if (UserId == 0) {
            System.out.println("Please login first to create an account.");
             return;
             }


            AccountDTO dto = new AccountDTO();
            dto.setUserId((int)UserId);
            dto.setAccountType(accountType);
            dto.setBalance(0.0);
            dto.setLocked(isLocked);

            // Call service to create account
            accountService.createAccount(dto);

            System.out.println("Account creation request completed.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }

    }
    
    private void handleDeposit()  {
        System.out.println("\n=== DEPOSIT MONEY ===");
        // TODO: Show user's accounts, get account selection and amount
        //System.out.println("TODO: Implement deposit logic using TransactionOrchestrator");
        if (UserId == 0) {
            System.out.println("Please login first to deposit into account.");
            return;
        }
        try {
            depositAndWithdrawOrchestrator.handleDeposit(UserId);
        }
        catch(BankingException | SQLException e){
            System.out.println("Error While performing Deposit into Account"+e.getMessage());
        }
    }
    
    private void handleWithdraw() {
        System.out.println("\n=== WITHDRAW MONEY ===");
        // TODO: Show user's savings accounts only, get account selection and amount
        //System.out.println("TODO: Implement withdrawal logic using TransactionOrchestrator");
        if (UserId == 0) {
            System.out.println("Please login first to withdraw from account.");
            return;
        }
        try {
            depositAndWithdrawOrchestrator.handleWithdraw(UserId);
        }
        catch (BankingException | SQLException e){
            System.out.println("Error While performing Withdrawal from Account"+e.getMessage());
        }

    }
    
    private void handleTransfer() {
        System.out.println("\n=== TRANSFER MONEY ===");
        // TODO: Show transfer options (Savings to Savings, Savings to FD)
        //System.out.println("TODO: Implement transfer logic using appropriate Orchestrator");
        System.out.println("Select type of Transaction");
        System.out.println("1. Self Transaction");
        System.out.println("2. User Transaction");
        System.out.println("3. Exit ");
        int input = Integer.parseInt(scanner.nextLine().trim());
        switch (input) {
            case 1:
                break;
            case 2:
                if (UserId == 0) {
                    System.out.println("Please login first to withdraw from account.");
                    return;
                }
                try {
                    transactOrchestrator.transactAmountBetweenUsers(UserId);
                } catch (BankingException e) {
                    System.out.println("Problem with transactions");
                } catch (SQLException e) {
                    System.out.println("SQL Error has Occurred");
                }
                break;
            case 3:
                showUserMenu();
                break;
            default:
                System.out.println("Please Select Correct Option");
                break;
        }
    }
    
    private void handleViewAccounts() {
        System.out.println("\n=== YOUR ACCOUNTS ===");
        System.out.println("Choose the option to display the accounts");


        if (UserId == 0) {
            System.out.println("please login first before ViewAccount");
        }
        List<AccountDTO> accountDTOs = accountService.getAccountsByUserId(UserId);

        for(AccountDTO dto: accountDTOs){
            System.out.printf("Account number: %s | Type: %s | Balance: %.2f | Interest: %f | createdAt:%s %n",
                    dto.getAccountNumber(),
                    dto.getAccountType(),
                    dto.getBalance(),
                    dto.getInterest(),
                    dto.getCreatedAt()
            );
        }

       // System.out.println("TODO: Implement account viewing using AccountService");
    }
    
    private void handleViewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        // TODO: Show user's accounts, let them select one, then show transaction history
        System.out.println("TODO: Implement transaction history using TransactionService");
        TransactionService transactionService = new TransactionService();
        try {
            List<TransactionDTO> listOfTransactions = transactionService.getTransactionHistoryById(UserId);

            if (listOfTransactions == null || listOfTransactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }
            // Table Header
            System.out.printf("%-15s %-12s %-12s %-20s %-15s %-15s %-10s%n",
                    "Transaction ID", "Type", "Amount", "Date", "From Account", "To Account", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------");

            // Table Rows
            for (TransactionDTO t : listOfTransactions) {
                System.out.printf("%-15s %-12s %-12.2f %-20s %-15s %-15s %-10s%n",
                        t.getTransaction_id(),
                        t.getTransaction_type(),
                        t.getAmount(),
                        t.getCreated_at(),
                        t.getFrom_account_id(),
                        t.getTo_account_id(),
                        t.getStatus());
            }
        } catch (BankingException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void handleUpdateProfile() {
        System.out.println("\n=== UPDATE PROFILE ===");

        try {
            // No username input needed

            System.out.print("Enter new full name: ");
            String fullName = scanner.nextLine();

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            System.out.print("Enter new phone number: ");
            String phone = scanner.nextLine();

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setFullName(fullName);
            updatedDTO.setEmail(email);
            updatedDTO.setPhone(phone);
            System.out.println(currentUsername);

            userOrchestrator.updateUserDetails(UserId, updatedDTO);

            System.out.println("Profile updated successfully!");

        } catch (Exception e) {
            System.err.println("Failed to update profile: " + e.getMessage());
        }
    }



    /**
     * Utility method to get user input with prompt.
     */
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Utility method to display error messages.
     */
    public void showError(String message) {
        System.err.println("ERROR: " + message);
    }
    
    /**
     * Utility method to display success messages.
     */
    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }
}
