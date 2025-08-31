# Java CLI Banking Application - Assignment CheckList

## Project Structure
| Status      | Task                                              | Notes                   |
|-------------|---------------------------------------------------|-------------------------|
| Given       | Create `com.bank.cli` package with Main class     | Entry point             |
| Given       | Create `com.bank.cli.display` package             | CLI output/menu classes |
| Completed   | Create `com.bank.user` package                    | User profile mgmt       |
| Completed   | Create `com.bank.account` package                 | Bank account mgmt       |
| Completed   | Create `com.bank.transaction` package             | Transaction logic       |
| Completed   | Create `com.bank.logging` package                 | Logging logic           |
| Given       | Create `com.bank.db` package                      | DB interaction layer    |
| Completed   | Create `com.bank.db.user` package                 | User DAO                |
| Completed   | Create `com.bank.db.account` package              | Account DAO             |
| Completed   | Create `com.bank.db.transaction` package          | Transaction DAO         |
| Completed   | Create `com.bank.db.logging` package              | Log DAO                 |
| Completed   | Create `com.bank.dto` package                     | DTO classes             |
| Completed   | Create `com.bank.mapper` package                  | Entity-DTO mappers      |
| Completed   | Create `com.bank.orchestrator` package            | Workflow coordinators   |
| Completed   | Create `com.bank.exception` package               | Custom exceptions       |

## User Authentication Module

| Status    | Task                                      | Notes                      |
|-----------|-------------------------------------------|----------------------------|
| Completed | Create `User` entity                      | username, password, etc.   |
| Completed | `UserService.createUserProfile()`         | User creation logic        |
| Completed | `UserService.validateUserCredentials()`   | Authentication check       |
| Completed | `UserService.updateContactInfo()`         | Profile updates            |
| Completed | `UserService.getUserProfile()`            | Profile retrieval          |
| Given     | Create `UserDTO`                          | DTO for customer           |
| Completed | Create `UserrMapper`                      | Entity to DTO conversion   |
| Completed | Create `UserDAO`                          | For DB operations          |
| Completed | Implement password hashing (e.g., BCrypt) | Password protection        |
| Completed | `AuthService.login()`                     | Login                      |
|           | `AuthService.logout()`                    | Logout                     |
| Completed | `AuthService.isAuthenticated()`           | Session validation         |
|           | Session management                        | Token/session tracking     |
|           | Create `LoginRequestDTO`, `SessionDTO`    | Auth data transfer         |
| Completed | Create `AuthRepository`                   | Authentication persistence |

## Account Management Module

| Status    | Task                                                    | Notes                    |
|-----------|---------------------------------------------------------|--------------------------|
| Completed | Create  `Account` entity                                | Common account fields    |
| Completed | Add auto-generated account number                       | Unique ID                |
| Completed | Add lock status to FD account                           | Locking mechanism        |
|           | Add maturity date to FD account                         | Maturity tracking        |
|           | Add interest rate to FD account                         | Interest logic           |
| Completed | Create corresponding DTOs                               | Data transfer objects    |
| Completed | Create account mappers                                  | Entity to DTO            |
| Completed | `AccountService.createSavingsAccount()`                 | Create savings account   |
| Completed | `AccountService.createFixedDepositAccount()`            | Create FD account        |
| Completed | `AccountService.getAccountsByUser()`                    | List customer accounts   |
| Completed | `AccountService.getAccountDetails()`                    | Account details          |
| Completed | `AccountService.updateAccountBalance()`                 | Balance updates          |
| Completed | Create `AccountRepository` interface and implementation | Account DB access        |
|           | Implement account ownership validation                  | Auth check               |


## Banking Transactions Module

| Status    | Task                                                       | Notes                           |
|-----------|------------------------------------------------------------|---------------------------------|
| Completed | Create `Transaction` entity with ID                        | Transaction model               |
| Completed | Create `Transaction Mappers`                               | Deposit logic                   |
| Completed | `TransactionService.deposit()`                             | Deposit logic                   |
| Completed | `TransactionService.withdraw()`                            | Withdrawal logic                |
| Completed | `TransactionService.getTransactionHistory()`               | View history                    |
| Completed | `TransactionService.createTransactionRecord()`             | Record creation                 |
| Completed | `TransferService.transferSavingsToSavings()`               | S → S transfer                  |
| Completed | `TransferService.transferSavingsToFixedDeposit()`          | S → FD transfer                 |
| Completed | `TransferService.transferSavingsToSavings` different User  | S → S transfer                  |
| Completed | Implement transfer validation logic                        | Transfer Rules (only Atomicity) |
| Completed | Create `TransferDTO`                                       | Transfer data object            |
| Completed | Create `TransactionDAO`                                    | Persist transactions            |
| Completed | Implement deposit rules (Savings, FD at creation/transfer) | Deposit constraints             |
| Completed | Implement withdrawal rules (Savings only)                  | Withdrawal constraints          |
| Completed | Prevent withdrawals from locked Fixed Deposits             | FD lock enforcement             |
| Completed | Implement S → S transfer rules                             | Validation rules                |
|           | Implement S → FD transfer rules (locks FD)                 | Lock after transfer             |
| Completed | Implement balance validation for all operations            | Sufficient funds check          |
| Completed | Implement balance updation for all operations              | Atomicity                       |

## Orchestrator Layer

| Status    | Task                                             | Notes                      |
|-----------|--------------------------------------------------|----------------------------|
| Completed | Create `TransactionOrchestrator` class           | Transaction coordination   |
| Completed | Implement `processDeposit()`                     | Deposit workflow           |
| Completed | Implement `processWithdrawal()`                  | Withdrawal workflow        |
| Completed | Add user authentication verification             | Security check             |
|           | Add account ownership verification               | Authorization check        |
|           | Add account balance validation                   | Balance check              |
| Completed | Integrate service method calls                   | Service orchestration      |
|           | Add operation logging                            | Audit trail                |
| Completed | Create `FixedDepositOrchestrator` class          | FD transfer coordination   |
| Completed | Implement `processSavingsToFDTransfer()`         | S → FD workflow            |
|           | Validate source account (Savings)                | Source validation          |
| Completed | Check sufficient funds                           | Transfer pre-check         |
|           | Create/update Fixed Deposit account              | FD management              |
|           | Lock FD funds                                    | Post-transfer locking      |
| Completed | Create transaction records                       | Audit and rollback-ready   |
|           | Calculate maturity metadata                      | FD lifecycle               |

## CLI Interface

| Status      | Task                                       | Notes                      |
|-------------|--------------------------------------------|----------------------------|
| Completed   | Create `MainMenuDisplay` class             | Main entry menu            |
|             | Add welcome message                        | User greeting              |
| Completed   | Add login option                           | Authentication             |
| Completed   | Add create customer profile option         | New user registration      |
| Completed   | Add exit option                            | Application shutdown       |
|             | Input validation and error handling        | Prevent invalid entries    |
| Completed   | Create `UserMenuDisplay` class             | Post-login menu            |
| Completed   | Add create bank account option             | Create Savings/FD          |
| Completed   | Add deposit money option                   | Deposit funds              |
| Completed   | Add withdraw money option                  | Withdraw funds             |
| Completed   | Add transfer money option                  | S → S / S → FD             |
| Completed   | Add view account details option            | Account summary            |
| Completed   | Add view transaction history option        | View past transactions     |
| Completed   | Add update profile info option             | Update contact             |
| Completed   | Add logout option                          | Session termination        |
| Completed   | Implement menu navigation logic            | Menu interaction           |
|             | Handle invalid input gracefully            | Robust UX                  |
|             | Create `InputHandler` class                | Central input validation   |
|             | Create `OutputFormatter` class             | Standardized formatting    |
|             | Implement password masking                 | Secure input               |
| Completed   | Create account display methods             | Tabular account view       |
| Completed   | Create transaction history display methods | Tabular history view       |
|             | Display success/error messages             | User feedback              |


## Logging System

| Status      | Task                                              | Notes                      |
|-------------|---------------------------------------------------|----------------------------|
| Completed   | Create `LogService` class                         | Core logging service       |
|             | Implement `logUserAction()`                       | Log CLI/user events        |
|             | Implement `logTransaction()`                      | Track banking ops          |
|             | Implement `logAuthentication()`                   | Login/logout logs          |
|             | Implement `logAccountCreation()`                  | Track account creation     |
| Completed   | Create `LogEntry` entity                          | Log model                  |
| Completed   | Create `LogDTO` and mapper classes                | Transfer log data          |
| Completed   | Create `LogRepository`                            | Store logs in DB           |
| Completed   | Log successful/failed login attempts              | Audit trail                |
|             | Log account creation                              | Account activity           |
| Completed   | Log all transactions                              | Transaction traceability   |
| Completed   | Log profile updates                               | User changes               |
| Completed   | Log system errors                                 | Debug/alerting             |

## Validation & Security

| Status    | Task                                             | Notes                      |
|-----------|--------------------------------------------------|----------------------------|
|           | Validate username format and uniqueness          | Prevent duplicates         |
| Completed | Validate password strength                       | Enforce security           |
| Completed | Validate contact info format                     | Email, phone, etc.         |
|           | Validate account numbers and amounts             | Format and integrity       |
|           | Validate transaction amounts                     | Positive, formatted        |
| Completed | Validate email/phone format                      | Regex or lib-based         |
|           | Secure session management                        | Prevent hijacking          |
|           | Validate user authorization                      | Ownership and role checks  |
|           | Implement account ownership verification         | Cross-check on ops         |
|           | Add rate limiting to login attempts              | Block brute force attacks  |


## Documentation

| Status | Task                                             | Notes                      |
|--------|--------------------------------------------------|----------------------------|
|        | Add JavaDoc to all public classes                | Class-level doc            |
|        | Add JavaDoc to all public methods                | Method-level doc           |
|        | Include param and return descriptions            | API clarity                |
|        | Add @throws docs for exceptions                  | Error behavior             |
|        | Add usage examples in complex classes            | Developer help             |
|        | Create comprehensive `README.md`                 | Project overview           |
|        | Add features and modules to README               | Showcase capability        |
| Given  | Add install instructions to README               | Setup steps                |
| Given  | Add how to run the app to README                 | CLI start guide            |
|        | Add sample user data to README                   | Test credentials           |
| Given  | Add package structure explanation                | Architecture overview      |
|        | Add sample API usage examples                    | Usage reference            |


## Test Cases (Stretch Goals)

| Status | Task                                    | Notes                      |
|-------|-----------------------------------------|----------------------------|
|       | Set up JUnit 5 and  Mockito             | Test framework             |
|       | Unit test `UserService` methods         | Business logic tests       |
|       | Unit test `AccountService` methods      | Balance/account tests      |
|       | Unit test `TransactionService` methods  | Deposits, withdrawals      |
|       | Unit test mapper classes                | Conversion logic           |
|       | Unit test orchestrator methods          | End-to-end validation      |
|       | Integration test: customer registration | Full flow test             |
|       | Integration test: account creation      | Entity + repo test         |
|       | Integration test: deposit/withdrawal    | Balance changes            |
|       | Integration test: transfers             | Funds movement             |
|       | Integration test: transaction history   | Display and storage        |
|       | Test edge/error scenarios               | Negative path testing      |
|       | Test DB interaction logic               | Repo + data validation     |

## Stretch Goals 

| Status    | Task                                             | Notes                      |
|-----------|--------------------------------------------------|----------------------------|
| Completed | Colored console output                           | CLI readability            |
| Completed | Formatted tables for accounts                    | Pretty print               |
|           | Progress indicators                              | Long task feedback         |
|           | Input history navigation                         | Improve UX                 |
|           | Command shortcuts                                | Power user support         |
|           | `application.properties` or YAML                 | Configurable constants     |
|           | Interest rate config per account type            | Business logic             |
|           | Transaction/account limits                       | Rule enforcement           |
|           | Default admin credentials                        | Admin bootstrap            |
|           | DB connection config                             | Externalize DB settings    |
|           | Interest calculation for FD                      | Compound interest          |
|           | Maturity date calculation                        | FD timelines               |
|           | Interest accrual service                         | Background job             |
|           | Auto interest posting                            | Monthly/Quarterly          |
|           | Early withdrawal penalties                       | Reduce payout              |
| Completed | Export transaction history to CSV                | File output                |
| Completed | Export transaction history to TXT                | Plain text logs            |
|           | Generate account statements                      | Monthly PDF/TXT            |
|           | Filter reports by date range                     | Time-based view            |
|           | Admin role with enhanced permissions             | Role-based access          |
|           | Admin login and session                          | Separate auth              |
|           | Admin dashboard (all accounts view)              | Monitoring panel           |
|           | Admin access to all transaction logs             | Auditing                   |
|           | User management for admins                       | Create, disable, reset     |
|           | Input localization support                       | Multi-language CLI         |
|           | Friendly date formatting                         | DD-MM-YYYY, etc.           |
|           | Graceful shutdown                                | Save + exit safety         |
|           | Data backup feature                              | Manual or auto export      |
|           | Transaction categorization                       | Spending tags              |
|           | Spending analytics                               | Pie chart, trend analysis  |
