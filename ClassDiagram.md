```mermaid
classDiagram
    %% Core Entities
    class UserEntity {
        -int id
        -String username
        -String passwordHash
        -String fullName
        -String email
        -String phone
        -String createdAt
        -String updatedAt
    }

    class AccountEntity {
        -String account_number
        -int user_id
        -String account_type
        -double balance
        -boolean is_locked
        -String account_created
        -String account_updated
        -String type
        -double interest
    }

    class TransactionEntity {
        -String transaction_id
        -String from_account_id
        -String to_account_id
        -String transaction_type
        -double amount
        -String description
        -String status
        -Timestamp created_at
    }

    class LogEntity {
        -int id
        -int user_id
        -LogDAO.Action action
        -String details
        -String ip_address
        -LogDAO.Status status
        -Timestamp timestamp
    }

    %% Services
    class LogService {
        +logIntoDB(logEntity: LogEntity)
    }

    class AccountService {
        +createAccount(accountDTO: AccountDTO)
        +generateUniqueAccountNumberUUID()
        +getAccountsByUserId(userId: int): List<AccountDTO>
    }

    class AuthService {
        +validateUserCredentials(username, password)
        +signInUser(username, password)
    }

    class TransactionService {
        +creditToAccount(accountId, amount)
        +debitFromAccount(accountId, amount)
        +getTransactionHistoryById(accountId): List<TransactionDTO>
    }

    class UserService {
        +createUser(userDTO: UserDTO)
        +displayProfile(userId: int)
        +updateUserProfile(userDTO: UserDTO)
    }

    %% Utils
    class PasswordUtil {
        +verifyPassword(password, hash)
        +hashPassword(password)
    }

    %% DTOs
    class UserDTO
    class AccountDTO
    class TransactionDTO
    class LogDTO

    %% Mappers
    class AccountMapper {
        +mapToAccountEntity()
        +mapToAccountEntityList()
        +dtoToEntity()
        +entityToDTO()
    }

    class LogMapper {
        +mapToLogEntity()
        +mapToLogEntityList()
        +logDtoToLogEntity()
        +logEntityToDTO()
    }

    class TransactionMapper {
        +mapToTransactionEntity()
        +transactionEntityToDto()
        +transactionDtoToEntity()
        +mapResultSetToEntity()
        +entityToTransactionDtoList()
        +mapToTransactionEntityList()
    }

    class UserMapper {
        +mapToUserEntity()
        +userEntityToDto()
        +userDtoToUserEntity()
        +mapToUserEntityList()
    }

    %% Orchestrators
    class DepositAndWithdrawOrchestrator {
        +selectAccountNumber()
        +handleDeposit()
        +handleWithdraw()
    }

    class TransactOrchestrator {
        +selectAccountNumber()
        +transactAmountBetweenUsers()
    }

    class UserOrchestrator {
        +signup()
        +displayProfile()
        +updateUserDetails()
    }

    %% Exceptions
    class AccountNotFoundException
    class BankingException
    class CreditFailureException
    class DebitFailureException
    class InsufficientFundsException
    class InvalidCredentialsException
    class TransactionFailureException
    class UserAlreadyExist
    class UserNotFoundException

    %% DAO Layer
    class AccountDAO {
        +insertAccountDetails()
        +getAccountDetails()
        +getAccountById()
        +updateAccountDetails()
        +getAccountsByUserId()
    }

    class DatabaseManager {
        +initializeDatabase()
        +createTables()
        +query(sql)
    }

    class LogDAO {
        +insertLog()
        +getAllLogs()
        enum Action
        enum Status
    }

    class TransactionDAO {
        +getTransactionsByAccountNumber()
        +saveTransaction()
        +updateStatus()
        enum TransactionType
        enum Status
    }

    class UserDAO {
        +createUser()
        +getUserById()
        +getUserByUsername()
        +updateUser()
    }

    %% Relationships
    UserEntity "1" --> "many" AccountEntity
    AccountEntity "1" --> "many" TransactionEntity
    UserService --> UserEntity
    UserService --> UserDTO
    AccountService --> AccountEntity
    AccountService --> AccountDTO
    TransactionService --> TransactionEntity
    TransactionService --> TransactionDTO
    LogService --> LogEntity
    LogService --> LogDTO

    UserDTO <.. UserMapper
    AccountDTO <.. AccountMapper
    TransactionDTO <.. TransactionMapper
    LogDTO <.. LogMapper

    DepositAndWithdrawOrchestrator --> AccountService
    DepositAndWithdrawOrchestrator --> TransactionService
    DepositAndWithdrawOrchestrator --> LogService

    TransactOrchestrator --> TransactionService
    TransactOrchestrator --> AccountService
    TransactOrchestrator --> LogService

    UserOrchestrator --> UserService
    UserOrchestrator --> AuthService
