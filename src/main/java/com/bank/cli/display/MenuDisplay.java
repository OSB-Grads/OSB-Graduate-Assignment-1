package com.bank.cli.display;

import com.bank.Orchestrators.DepositAndWithdrawOrchestrator;
import com.bank.Orchestrators.TransactOrchestrator;
import com.bank.Orchestrators.TransferOrchestrator;
import com.bank.Orchestrators.UserOrchestrator;
import com.bank.dto.AccountDTO;
import com.bank.dto.TransactionDTO;
import com.bank.dto.UserDTO;
import com.bank.exception.*;
import com.bank.services.AccountService;
import com.bank.services.AuthService;
import com.bank.services.TransactionService;
import com.bank.util.ConsoleColor;
import com.bank.util.DateUtil;
import com.bank.util.InputValidator;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;



/**
 * Handles all CLI menu display and user input.
 * This class is responsible for showing menus and collecting user choices.
 */
public class MenuDisplay {

    private int  UserId = 0;
    private String currentUsername;
    private Scanner scanner;
    private final AccountService accountService;
    private final AuthService authService;
    private final UserOrchestrator userOrchestrator;
    private final DepositAndWithdrawOrchestrator depositAndWithdrawOrchestrator;
    private final TransactOrchestrator transactOrchestrator;
    private TransferOrchestrator transferOrchestrator;

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
            System.out.println(ConsoleColor.PURPLE+"1. Login"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"2. Create Customer Profile"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"3. Exit"+ConsoleColor.RESET);
            System.out.print(ConsoleColor.BLUE+"Please select an option (1-3): "+ConsoleColor.RESET);

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
                        System.out.println(ConsoleColor.GREEN+"Thank you for using CLI Banking Application!"+ConsoleColor.RESET);
                        running = false;
                        break;
                    default:
                        System.out.println(ConsoleColor.YELLOW+"Invalid option. Please select 1, 2, or 3."+ConsoleColor.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.RED+"Invalid input. Please enter a number."+ConsoleColor.RESET);
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
            System.out.println(ConsoleColor.PURPLE+"1. Create Bank Account"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"2. Deposit Money"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"3. Withdraw Money"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"4. Transfer Money"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"5. View Account Details"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"6. View Transaction History"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"7. Update Profile Info"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"8. View User Profile"+ConsoleColor.RESET);
            System.out.println(ConsoleColor.PURPLE+"9. Logout"+ConsoleColor.RESET);
            System.out.print(ConsoleColor.PURPLE+"Please select an option (1-8): "+ConsoleColor.RESET);
            
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
                        viewUserProfile();
                        break;
                    case 9:
                        System.out.println(ConsoleColor.BLUE+"Logging out..."+ConsoleColor.RESET);
                        UserId =0;
                        loggedIn = false;
                        break;
                    default:
                        System.out.println(ConsoleColor.YELLOW+"Invalid option. Please select 1-8."+ConsoleColor.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.RED+"Invalid input. Please enter a number."+ConsoleColor.RESET);
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
        System.out.print(ConsoleColor.BLUE+"Username: "+ConsoleColor.RESET);
        String username = scanner.nextLine().trim();
        System.out.print(ConsoleColor.BLUE+"Password: "+ConsoleColor.RESET);
        String password = scanner.nextLine().trim();

        UserDTO DTO = null;
        try {
            DTO = authService.validateUserCredentials(username,password);
            UserId=DTO.getId();
            currentUsername=DTO.getUsername();
            showUserMenu();
        } catch (Exception e) {
            if(e instanceof InvalidCredentialsException){
                showError(e.getMessage());
            }
            else if(e instanceof UserNotfoundException){
                showError(e.getMessage());
            }else{
                showError(new UserNotfoundException().getMessage());
            }
        }

    }

    private void handleCreateProfile() {
        String username;
        do {
            System.out.print(ConsoleColor.BLUE + "Username: " + ConsoleColor.RESET);
            username = scanner.nextLine().trim();
            if (!InputValidator.isValidUsername(username)) {
                System.out.println(ConsoleColor.YELLOW+"Invalid username. Must be at least 5 characters, only letters & numbers allowed."+ConsoleColor.RESET);
                username = null;
            }
        } while (username == null);

        String password;
        do {
            System.out.print(ConsoleColor.BLUE + "Password: " + ConsoleColor.RESET);
            password = scanner.nextLine().trim();
            if (!InputValidator.isValidPassword(password)) {
                System.out.println(ConsoleColor.YELLOW+"Invalid password. Must be at least 8 chars, include 1 number and 1 special character."+ConsoleColor.RESET);
                password = null;
            }
        } while (password == null);

        String fullName;
        do {
            System.out.print(ConsoleColor.BLUE + "Full Name: " + ConsoleColor.RESET);
            fullName = scanner.nextLine().trim();
            if (!InputValidator.isValidFullName(fullName)) {
                System.out.println(ConsoleColor.YELLOW+"Invalid full name. Only letters and spaces, min 2 characters."+ConsoleColor.RESET);
                fullName = null;
            }
        } while (fullName == null);

        String email;
        do {
            System.out.print(ConsoleColor.BLUE + "Email: " + ConsoleColor.RESET);
            email = scanner.nextLine().trim();
            if (!InputValidator.isValidEmail(email)) {
                System.out.println(ConsoleColor.YELLOW+"Invalid email format. Example: user@example.com"+ConsoleColor.RESET);
                email = null;
            }
        } while (email == null);

        String phone;
        do {
            System.out.print(ConsoleColor.BLUE + "Phone: " + ConsoleColor.RESET);
            phone = scanner.nextLine().trim();
            if (!InputValidator.isValidPhone(phone)) {
                System.out.println(ConsoleColor.YELLOW+"Invalid phone number. Must be 10 digits."+ConsoleColor.YELLOW);
                phone = null;
            }
        } while (phone == null);

        try {
            userOrchestrator.signup(UserId, username, password, fullName, email, phone);  // <-- underlined change
            showSuccess("Profile created successfully!");
        } catch(UserAlreadyExist e) {
            showError(e.getMessage());
        }
        catch (Exception e) {
            showError(ConsoleColor.RED+"Failed to create profile: " + e.getMessage()+ConsoleColor.RESET);
        }
    }


    private void handleCreateAccount() {
        System.out.println("\n=== CREATE BANK ACCOUNT ===");
        System.out.println(ConsoleColor.PURPLE+"1. Savings Account"+ConsoleColor.RESET);
        System.out.println(ConsoleColor.PURPLE+"2. Fixed Deposit Account"+ConsoleColor.RESET);
        System.out.print(ConsoleColor.BLUE+"Select account type (1-2): "+ConsoleColor.RESET);

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
                    System.out.println(ConsoleColor.YELLOW+"Invalid account type selected."+ConsoleColor.RESET);
                    return;
            }

            if (UserId == 0) {
            System.out.println(ConsoleColor.YELLOW+"Please login first to create an account."+ConsoleColor.RESET);
             return;
             }


            AccountDTO dto = new AccountDTO();
            dto.setUserId((int)UserId);
            dto.setAccountType(accountType);
            dto.setBalance(0.0);
            dto.setLocked(isLocked);

            // Call service to create account
            accountService.createAccount(dto);

            System.out.println(ConsoleColor.GREEN+"Account creation request completed."+ConsoleColor.RESET);

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.RED+"Invalid input. Please enter a number."+ConsoleColor.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColor.RED+"Error creating account: " + e.getMessage()+ConsoleColor.RESET);
        }

    }

    private void handleDeposit()  {
        System.out.println("\n=== DEPOSIT MONEY ===");
        // TODO: Show user's accounts, get account selection and amount
        //System.out.println("TODO: Implement deposit logic using TransactionOrchestrator");
        if (UserId == 0) {
            System.out.println(ConsoleColor.YELLOW+"Please login first to deposit into account."+ConsoleColor.RESET);
            return;
        }
        try {
            depositAndWithdrawOrchestrator.handleDeposit(UserId);
        }
        catch(BankingException | SQLException e){
            System.out.println(ConsoleColor.RED+"Error While performing Deposit into Account"+e.getMessage()+ConsoleColor.RESET);
        }
    }

    private void handleWithdraw() {
        System.out.println("\n=== WITHDRAW MONEY ===");
        // TODO: Show user's savings accounts only, get account selection and amount
        //System.out.println("TODO: Implement withdrawal logic using TransactionOrchestrator");
        if (UserId == 0) {
            System.out.println(ConsoleColor.YELLOW+"Please login first to withdraw from account."+ConsoleColor.RESET);
            return;
        }
        try {
            depositAndWithdrawOrchestrator.handleWithdraw(UserId);
        }
        catch (BankingException | SQLException e){
            System.out.println(ConsoleColor.RED+"Error While performing Withdrawal from Account"+e.getMessage()+ConsoleColor.RESET);
        }

    }

    private void handleTransfer()  {
        System.out.println("\n=== TRANSFER MONEY ===");
        // TODO: Show transfer options (Savings to Savings, Savings to FD)
        //System.out.println("TODO: Implement transfer logic using appropriate Orchestrator");
        System.out.println("Select type of Transaction");
        System.out.println(ConsoleColor.PURPLE+"1. Self Transaction"+ConsoleColor.RESET);
        System.out.println(ConsoleColor.PURPLE+"2. User Transaction"+ConsoleColor.RESET);
        System.out.println(ConsoleColor.PURPLE+"3. Exit "+ConsoleColor.RESET);
        int input = Integer.parseInt(scanner.nextLine().trim());
        switch (input) {
            case 1:if (UserId == 0) {
                System.out.println(ConsoleColor.YELLOW+"Please login first to withdraw from account."+ConsoleColor.RESET);
                return;
            }try {
                transferOrchestrator.transfer(UserId);
                System.out.println(ConsoleColor.GREEN+"Transaction Successful :)"+ConsoleColor.RESET);
            } catch (BankingException e) {
                System.out.println(ConsoleColor.RED+"Problem with transactions"+ConsoleColor.RESET);
            } catch (SQLException e) {
                System.out.println(ConsoleColor.RED+"SQL Error has Occurred"+ConsoleColor.RESET);
            }
                break;
            case 2:
                if (UserId == 0) {
                    System.out.println(ConsoleColor.YELLOW+"Please login first to withdraw from account."+ConsoleColor.RESET);
                    return;
                }
                try {
                    boolean trans=transactOrchestrator.transactAmountBetweenUsers(UserId);
                    if(trans) {
                        System.out.println(ConsoleColor.GREEN+"Transaction Successful :)"+ConsoleColor.RESET);
                    }
                    else{
                        System.out.println(ConsoleColor.RED+"Transaction Failed"+ConsoleColor.RESET);
                    }
                }
                catch(AccountNotFoundException e){
                    System.out.println(ConsoleColor.RED+"User Account Not Found"+ConsoleColor.RESET);
                }
                catch (BankingException e) {
                    System.out.println(ConsoleColor.RED+"Problem with transactions"+ConsoleColor.RESET);
                } catch (SQLException e) {
                    System.out.println(ConsoleColor.RED+"SQL Error has Occurred"+ConsoleColor.RESET);
                }
                break;
            case 3:
                showUserMenu();
                break;
            default:
                System.out.println(ConsoleColor.YELLOW+"Please Select Correct Option"+ConsoleColor.RESET);
                break;
        }
    }

    private void handleViewAccounts() {

        if (UserId == 0) {
            System.out.println(ConsoleColor.YELLOW+"Please login first before ViewAccount"+ConsoleColor.RESET);
            return ;
        }
        List<AccountDTO> accountDTOs = accountService.getAccountsByUserId(UserId);

        if (accountDTOs == null || accountDTOs.isEmpty()) {
            System.out.println(ConsoleColor.YELLOW+"No User Accounts found."+ConsoleColor.RESET);
            System.out.print(ConsoleColor.BLUE+"Would you like to create a new account? (yes/no): "+ConsoleColor.RESET);


            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("yes") || choice.equals("y")) {
                handleCreateAccount();
            } else {
                System.out.println("Returning to user menu...");
            }
            return;
        }
        System.out.println("\n=== YOUR ACCOUNTS ===");

        System.out.printf("%-15s %-10s %-12s %-10s %-20s%n",
                "Account No", "Type", "Balance", "Interest", "Created At");

        System.out.println("-------------------------------------------------------------");

        for (AccountDTO dto : accountDTOs) {
            System.out.printf("%-15s %-10s %-12.2f %-10.2f %-20s%n",
                    dto.getAccountNumber(),
                    dto.getAccountType(),
                    dto.getBalance(),
                    dto.getInterest(),
                    DateUtil.formatStringDate(dto.getCreatedAt()));
        }

       // System.out.println("TODO: Implement account viewing using AccountService");
    }

    private void handleViewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        // TODO: Show user's accounts, let them select one, then show transaction history
        //System.out.println("TODO: Implement transaction history using TransactionService");
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
            System.out.println("------------------------------------------------------------------------------------------------------------------");

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

            System.out.print(ConsoleColor.BLUE+"Enter new full name: "+ConsoleColor.RESET);
            String fullName = scanner.nextLine();

            System.out.print(ConsoleColor.BLUE+"Enter new email: "+ConsoleColor.RESET);
            String email = scanner.nextLine();

            System.out.print(ConsoleColor.BLUE+"Enter new phone number: "+ConsoleColor.RESET);
            String phone = scanner.nextLine();

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setFullName(fullName);
            updatedDTO.setEmail(email);
            updatedDTO.setPhone(phone);
            System.out.println(currentUsername);

            userOrchestrator.updateUserDetails(UserId, updatedDTO);

            System.out.println(ConsoleColor.GREEN+"Profile updated successfully!"+ConsoleColor.RESET);

        } catch (Exception e) {
            System.err.println(ConsoleColor.RED+"Failed to update profile: " + e.getMessage()+ConsoleColor.RESET);
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
        System.err.println(ConsoleColor.RED+"ERROR: " + message+ConsoleColor.RESET);
    }

    /**
     * Utility method to display success messages.
     */
    public void showSuccess(String message) {
        System.out.println(ConsoleColor.GREEN+"SUCCESS: " + message+ConsoleColor.RESET);
    }
}
