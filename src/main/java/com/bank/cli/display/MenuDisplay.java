package com.bank.cli.display;
import com.bank.dto.AccountDTO;
import com.bank.dto.UserDTO;
import com.bank.services.AccountService;
import com.bank.services.AuthService;

import java.util.Scanner;

/**
 * Handles all CLI menu display and user input.
 * This class is responsible for showing menus and collecting user choices.
 */
public class MenuDisplay {
    private long UserId;
    private Scanner scanner;
    private final AccountService accountService;
    private final AuthService authService;

    public MenuDisplay(AccountService accountService, AuthService authService) {
        this.scanner = new Scanner(System.in);
        this.accountService=accountService;
        this.authService=authService;

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
    
    // TODO: Implement these methods by calling appropriate services/orchestrators
    
    private void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        UserDTO DTO=authService.validateUserCredentials(username,password);
        UserId=DTO.getId();
        if (valid) {
            showSuccess("Login successful!");
            showUserMenu();
        } else {
            showError("Invalid username or password.");
        }
    } catch (Exception e) {
        showError("Login failed: " + e.getMessage());
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

//        try {
//            userOrchestrator.signup(username, password, fullName, email, phone);  // <-- underlined change
//            showSuccess("Profile created successfully!");
//        } catch (Exception e) {
//            showError("Failed to create profile: " + e.getMessage());
//        }
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


//            if (currentUserId == null) {
//                System.out.println("Please login first to create an account.");
//                return;
//            }


            AccountDTO dto = new AccountDTO(accountEntity.getAccount_number(), accountEntity.getUser_id(), accountEntity.getAccount_type(), accountEntity.getBalance(), accountEntity.isIs_locked(), accountEntity.getAccount_created(), accountEntity.getAccount_updated());
//            dto.setUserId(currentUserId);
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
    
    private void handleDeposit() {
        System.out.println("\n=== DEPOSIT MONEY ===");
        // TODO: Show user's accounts, get account selection and amount
        System.out.println("TODO: Implement deposit logic using TransactionOrchestrator");
    }
    
    private void handleWithdraw() {
        System.out.println("\n=== WITHDRAW MONEY ===");
        // TODO: Show user's savings accounts only, get account selection and amount
        System.out.println("TODO: Implement withdrawal logic using TransactionOrchestrator");
    }
    
    private void handleTransfer() {
        System.out.println("\n=== TRANSFER MONEY ===");
        // TODO: Show transfer options (Savings to Savings, Savings to FD)
        System.out.println("TODO: Implement transfer logic using appropriate Orchestrator");
    }
    
    private void handleViewAccounts() {
        System.out.println("\n=== YOUR ACCOUNTS ===");
        // TODO: Call AccountService to get user's accounts and display them
        System.out.println("TODO: Implement account viewing using AccountService");
    }
    
    private void handleViewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        // TODO: Show user's accounts, let them select one, then show transaction history
        System.out.println("TODO: Implement transaction history using TransactionService");
    }
    
    private void handleUpdateProfile() {
        System.out.println("\n=== UPDATE PROFILE ===");
        // TODO: Allow user to update contact information
        System.out.println("TODO: Implement profile update using UserService");
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
