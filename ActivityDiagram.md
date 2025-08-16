flowchart TD
Start(["Start"]) --> ShowMainMenu["Display Main Menu"]

    ShowMainMenu --> Choice1{"Select Option?"}
    Choice1 -->|1: Login| HandleLogin["handleLogin()"]
    Choice1 -->|2: Create User| HandleCreateProfile["handleCreateProfile()"]
    Choice1 -->|3: Exit| ExitMsg["Print Exit Message & End"]
    Choice1 -->|Invalid Input| ShowMainMenu

    %% Login Flow
    HandleLogin --> ValidateLogin{"Credentials valid?"}
    ValidateLogin -->|Yes| ShowUserMenu["Display User Menu"]
    ValidateLogin -->|No| ErrorLogin["Print Error - Invalid Credentials"] --> ShowMainMenu

    %% Create User Flow
    HandleCreateProfile --> CheckUsername{"Username exists?"}
    CheckUsername -->|Yes| ErrorUsername["Print Error - Enter unique username"] --> ShowMainMenu
    CheckUsername -->|No| InputDetails["Prompt fullname, email, phone"] --> CreateProfile["Create user & profile in DB"] --> SuccessSignup["Print Success Message"] --> ShowMainMenu

    %% User Menu Options
    ShowUserMenu --> UserChoice{"Select User Option"}
    UserChoice -->|1: Create Bank Account| HandleCreateAccount["handleCreateAccount()"] --> ShowUserMenu
    UserChoice -->|2: Deposit Money| HandleDeposit["handleDeposit()"] --> ShowUserMenu
    UserChoice -->|3: Withdraw Money| HandleWithdraw["handleWithdraw()"]

    %% Withdraw Rules
    HandleWithdraw --> IsFD{"Is account FD?"}
    IsFD -->|Yes| ErrorFD["Error - FD withdrawal not allowed"] --> ShowUserMenu
    IsFD -->|No| CheckBalance{"Sufficient balance?"}
    CheckBalance -->|Yes| DeductBalance["Deduct amount from DB"] --> SuccessWithdraw["Print Withdrawal Successful"] --> ShowUserMenu
    CheckBalance -->|No| ErrorBalance["Error - Insufficient Balance"] --> ShowUserMenu

    UserChoice -->|4: Transfer Money| HandleTransfer["handleTransfer()"]
    HandleTransfer --> ValidTransfer{"Is transfer valid?"}
    ValidTransfer -->|Yes| DoTransfer["Debit & Credit in DB"] --> SuccessTransfer["Print Transfer Successful"] --> ShowUserMenu
    ValidTransfer -->|No| ErrorTransfer["Print Error - Not allowed"] --> ShowUserMenu

    UserChoice -->|5: View Account Details| HandleViewAccounts["handleViewAccounts()"] --> ShowUserMenu
    UserChoice -->|6: View Transaction History| HandleTransactionHistory["handleViewTransactionHistory()"] --> ShowUserMenu
    UserChoice -->|7: Update Profile Info| HandleUpdateProfile["handleUpdateProfile()"] --> ShowUserMenu
    UserChoice -->|8: Logout| LogoutMsg["Print 'Logging out...'"] --> ShowMainMenu
    UserChoice -->|Invalid Input| ShowUserMenu

    ExitMsg --> End(["End"])
