sequenceDiagram
participant U as User (CLI)
participant M as Main Menu
participant US as UserService
participant DB as Database
participant UM as User Menu
participant AS as AccountService
participant TS as TransactionService
participant LS as LoggingService

    Note over U,M: Application starts → Show Main Menu

    U->>M: Select "Create User"
    M->>US: Pass username, password
    US->>DB: Check if username exists
    alt Username exists
        US-->>U: Error - "Enter unique username"
        LS->>DB: Log failed signup
        M-->>U: Show Main Menu
    else Username does not exist
        US->>U: Prompt for fullname, email, phone
        US->>DB: Create user & profile
        US-->>U: Success message
        LS->>DB: Log successful signup
        M-->>U: Show Main Menu
    end

    U->>M: Select "Login"
    M->>DB: Validate username & hashed password
    alt Invalid credentials
        M-->>U: Error - Invalid credentials
        LS->>DB: Log failed login
        M-->>U: Show Main Menu
    else Valid credentials
        LS->>DB: Log successful login
        M-->>UM: Show User Menu
    end

    %% User Menu Operations
    U->>UM: Select "Create Bank Account"
    UM->>AS: Create Savings or FD Account
    AS->>DB: Insert account details
    AS-->>U: Success message
    LS->>DB: Log account creation
    UM-->>U: Show User Menu

    U->>UM: Select "Deposit Money"
    UM->>AS: Prompt account selection
    AS->>DB: Update balance
    AS-->>U: Deposit successful
    LS->>DB: Log deposit
    UM-->>U: Show User Menu

    U->>UM: Select "Withdraw Money"
    UM->>AS: Prompt account selection
    alt Account is FD
        AS-->>U: Error - FD withdrawal not allowed
        LS->>DB: Log failed withdrawal
    else Account is Savings
        alt Sufficient balance
            AS->>DB: Deduct amount
            AS-->>U: Withdrawal successful
            LS->>DB: Log withdrawal
        else Insufficient balance
            AS-->>U: Error - Insufficient balance
            LS->>DB: Log failed withdrawal
        end
    end
    UM-->>U: Show User Menu

    U->>UM: Select "Transfer Money"
    UM->>TS: Prompt transfer type (self or other user)
    alt Invalid transfer (FD to Savings)
        TS-->>U: Error - Not allowed
        LS->>DB: Log failed transfer
    else Valid transfer
        TS->>DB: Perform debit & credit
        TS-->>U: Transfer successful
        LS->>DB: Log transfer
    end
    UM-->>U: Show User Menu

    U->>UM: Select "View Account Details"
    UM->>DB: Fetch account details
    UM-->>U: Display details
    UM-->>U: Show User Menu

    U->>UM: Select "View Transaction History"
    UM->>DB: Fetch transactions
    UM-->>U: Display transactions
    UM-->>U: Show User Menu

    U->>UM: Select "Update Profile Info"
    UM->>US: Update profile (no username/pass)
    US->>DB: Save changes
    US-->>U: Success message
    LS->>DB: Log profile update
    UM-->>U: Show User Menu

    U->>UM: Select "View User Profile"
    UM->>DB: Fetch profile
    UM-->>U: Display profile (no username/pass)
    UM-->>U: Show User Menu

    U->>UM: Select "Logout"
    LS->>DB: Log logout
    UM-->>M: Return to Main Menu
