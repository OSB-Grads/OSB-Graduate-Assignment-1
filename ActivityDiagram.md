flowchart TD

%% ========= START ========= %%
Start(["Start Application"]) --> InitDB["Initialize Database (DatabaseManager.java) and App Context "]
InitDB --> LoadMainMenu["Show Main Menu (showMainMenu.java)"]
showMainMenu --> DisplayOptions["Display Options: 
         1. Login 
         2. Create Profile 
         3. Exit"]

DisplayOptions --> showUserMenu{"User Input?"}

%% ========= MAIN MENU OPTIONS ========= %%
showUserMenu -->|1: Login| HandleLogin["handleLogin()"]
showUserMenu -->|2: Create Profile| HandleCreateProfile["handleCreateProfile()"]
showUserMenu -->|3: Exit| ExitApp
showUserMenu -->|Invalid Input| InvalidMain["Print Error: Invalid Input"] -->  ShowMainMenu


%% ========== LOGIN FLOW ========== %%
StartLogin --> AskLoginCreds["Prompt Username & Password"]
AskLoginCreds --> ValidateLogin["UserService.validateLogin()"]
ValidateLogin -->|Invalid| LoginFail["Print 'Invalid Credentials'"] --> DisplayOptions
ValidateLogin -->|Valid| LoadProfile["ProfileService.getProfileByUserId()"]
LoadProfile --> LoadAccounts["AccountService.getAccountsByUserId()"]
LoadAccounts --> ShowUserMenu["Display User Menu (UserMenu.java)"]

%% ========== CREATE PROFILE FLOW ========== %%
StartCreateProfile --> AskNewCreds["Prompt New Username & Password"]
AskNewCreds --> CheckExistUser["UserService.isUserExist()"]
CheckExistUser -->|Yes| ErrorUserExists["Print 'Username Exists'"] --> DisplayOptions
CheckExistUser -->|No| AskProfileDetails["Prompt FullName, Email, Phone"]
AskProfileDetails --> SaveUser["UserDAO.createUser()"]
SaveUser --> SaveProfile["ProfileDAO.createProfile()"]
SaveProfile --> ProfileCreated["Print 'Profile Created Successfully'"] --> DisplayOptions

%% ========== USER MENU FLOW ========== %%
ShowUserMenu --> UserMenuChoice{"Select Option:"}

UserMenuChoice -->|1. Create Account| Accont Service
UserMenuChoice -->|2. Deposit Money| DepositAndWithdraw Orchestration
UserMenuChoice -->|3. Withdraw Money| DepositAndWithdraw Orchestration
UserMenuChoice -->|4. Transfer Money| TransferOrchestration And Transact Orchestration
UserMenuChoice -->|5. View Accounts| Accounts Service
UserMenuChoice -->|6. View Transactions| Transaction Service
UserMenuChoice -->|7. Download Transactions| FileDownloader 
UserMenuChoice -->|8. Update Profile| User Orchestration
UserMenuChoice -->|9. View User Profile| User Orchestration
UserMenuChoice -->|10. Logout| ---> ShowMainMenu

%% ========== CREATE ACCOUNT FLOW ========== %%
CreateAccFlow --> AskAccDetails["Prompt: Account Type, Initial Deposit"]
AskAccDetails --> SaveAccount["AccountService.createAccount()"]
SaveAccount --> AccountSuccess["Print 'Account Created'"] --> ShowUserMenu

%% ========== DEPOSIT FLOW ========== %%
DepositFlow --> GetDepositInput[" Account Number + Amount"]
AskDepositInput --> ValidateAccount["AccountDAO.getAccountById()"]
ValidateAccount -->|Invalid| DepositError[AccuntNotFound Exception (Return null)] --> ShowUserMenu
ValidateAccount -->|Valid| DepositOrch["DepositAndWithdrawOrchestrator.handleDeposit()"]
DepositOrch --> UpdateBalance["AccountDAO.updateBalance()"]
UpdateBalance --> CreateDepositTxn["TransactionDAO.saveTransaction(DEPOSIT)"]
CreateDepositTxn --> DepositSuccess["Print 'Deposit Successful'"] --> ShowUserMenu

%% ========== WITHDRAW FLOW ========== %%
WithdrawFlow --> GetWithdrawInput[" Account Number + Amount"]
AskWithdrawInput --> GetAccount["AccountDAO.getAccountById()"]
GetAccount -->|FD Account| FDNotAllowed["Print 'Withdrawal not allowed for FD'"] --> ShowUserMenu
GetAccount -->|Non-FD| CheckBalance["Is Balance Sufficient?"]
CheckBalance -->|No| InsuffFunds[InsuffiecientFundsException] --> ShowUserMenu
CheckBalance -->|Yes| WithdrawOrch["DepositAndWithdrawOrchestrator.handleWithdraw()"]
WithdrawOrch --> DeductAmt["AccountDAO.updateBalance()"]
DeductAmt --> CreateWithdrawTxn["TransactionDAO.saveTransaction(WITHDRAW)"]
CreateWithdrawTxn --> WithdrawSuccess["Print 'Withdrawal Successful'"] --> ShowUserMenu

%% ========== TRANSFER FLOW ========== %%
TransferFlow --> AskTransferType[ 
    1. Self Transaction 
    2.Different User Transaction ]
Self Transaction --> Transfer Orchestration ---> AskTransferInput [" Input From, To Account Numbers + Amount"]
AskTransferInput --> ValidateFromTo["Check both accounts exist & different owners"]
ValidateFromTo -->|Invalid| InvalidTransfer["Print 'Invalid Transfer Details'"] --> ShowUserMenu
ValidateFromTo --> CheckTransferBalance["Check if FROM account has balance"]
CheckTransferBalance -->|No| TransferInsuffFunds["Print 'Insufficient Balance'"] --> ShowUserMenu
CheckTransferBalance -->|Yes| TransferOrch["TransferOrchestrator.handleTransfer()"]
TransferOrch --> DebitFrom["AccountDAO.updateBalance(FROM)"]
DebitFrom --> CreditTo["AccountDAO.updateBalance(TO)"]
CreditTo --> CreateTransferTxn["TransactionDAO.createTransaction(TRANSFER)"]
saveTransferTxn --> TransferSuccess["Print 'Transfer Successful'"] --> ShowUserMenu
If DebitFrom or CreditTo fails falls in Exception and re-do the completed function.


User Transaction --> Transfer Orchestration ---> AskTransferInput [" Input From, Enter To Account Numbers + Amount"]
ValidateFromTo -->|Invalid| InvalidTransfer["Print 'Invalid Transfer Details'"] --> ShowUserMenu
ValidateFromTo --> CheckTransferBalance["Check if FROM account has balance"]
CheckTransferBalance -->|No| TransferInsuffFunds["Print 'Insufficient Balance'"] --> ShowUserMenu
CheckTransferBalance -->|Yes| TransferOrch["TransferOrchestrator.handleTransfer()"]
TransferOrch --> DebitFrom["AccountDAO.updateBalance(FROM)"]
DebitFrom --> CreditTo["AccountDAO.updateBalance(TO)"]
CreditTo --> CreateTransferTxn["TransactionDAO.createTransaction(TRANSFER)"]
saveTransferTxn --> TransferSuccess["Print 'Transfer Successful'"] --> ShowUserMenu
If DebitFrom or CreditTo fails falls in Exception and re-do the completed function.


%% ========== VIEW ACCOUNTS FLOW ========== %%
ViewAccFlow --> FetchAccounts["AccountService.getAccountsByUserId()"]
FetchAccounts --> DisplayAccounts["Print Accounts with Balance + Type"] --> ShowUserMenu

%% ========== VIEW TRANSACTIONS FLOW ========== %%
ViewTxnFlow --> AskAccountsForTxn["
1. One Specific Account
2. All Accounts"]
One Specific Accont ---> GetAccountForTxn --> FetchTxns for Specific Account["TransactionService.getTransactionsByAccountId()"]
All Accounts ---> GetAccounts --> FetchTxns for all Accounts["TransactionService.getTransactionHistoryForUser()"]
FetchTxns --> DisplayTxns["Print Transactions (Transaction ID, Type, Amount, Date, From Account, To Account )"] --> ShowUserMenu

%% ========== DOWNLOAD TRANSACTIONS ========== %%
ViewAccFlow --> FetchTransactions["TransactionService.getTransactionHistoryById()"]
FetchTxns --> Display Download format [1. .csv 
2. .txt ] 
.csv ---> fileDownloader.downloadAsCSV ---> ShowUserMenu 
.txt ---> fileDownloader.downloadASTXT ---> ShowUserMenu 

%% ========== UPDATE PROFILE FLOW ========== %%
UpdateProfileFlow --> AskNewProfile["Prompt New FullName, Email, Phone"]
AskNewProfile --> UpdateProfileDAO["ProfileDAO.updateProfile()"]
UpdateProfileDAO --> ProfileUpdateSuccess["Print 'Profile Updated Successfully'"] --> ShowUserMenu

%% ========== VIEW USER PROFILE FLOW ========== %%
UserOrchestration ---> .displayProfile() ---> UserDAO.getUserById ---> Diplay [ Full Name, Email,Phone]

%% ========== LOGOUT FLOW ========== %%
DoLogout --> LogoutMsg["Print 'Logging out...'"] --> DisplayOptions

%% ========== EXIT ========== %%
ExitApp --> ExitMsg["Print 'Thank you for using the Bank App'"]
ExitMsg --> End(["End Application"])
