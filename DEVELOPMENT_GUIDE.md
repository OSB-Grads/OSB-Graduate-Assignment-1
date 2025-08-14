# Development Guide for CLI Banking Application

This guide will help you implement the banking application step by step.

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Running the Application
```bash
# Compile and run
mvn compile exec:java

# Or build and run JAR
mvn package
java -jar target/cli-banking-app-1.0.0.jar
```

## Implementation Steps

### Phase 1: Basic Setup ✅ (Already Done)
- [x] Project structure created
- [x] Database layer implemented
- [x] Sample DTOs created
- [x] Custom exceptions defined
- [x] Main class and MenuDisplay created

### Phase 2: Core Services (Your Task)

#### 2.1 Implement AuthService
**Location:** `src/main/java/com/bank/auth/AuthService.java`

**Key Methods:**
```java
public UserDTO login(String username, String password) throws InvalidCredentialsException
public void logout(int userId)
public String hashPassword(String password)
public boolean verifyPassword(String password, String hashedPassword)
```

**Database Operations Needed:**
- Query user by username
- Verify password hash
- Log login attempts

#### 2.2 Implement UserService
**Location:** `src/main/java/com/bank/user/UserService.java`

**Key Methods:**
```java
public UserDTO createUser(String username, String password, String fullName, String email, String phone)
public UserDTO getUserById(Long userId)
public UserDTO updateUserProfile(Long userId, String email, String phone)
```

#### 2.3 Implement AccountService
**Location:** `src/main/java/com/bank/account/AccountService.java`

**Key Methods:**
```java
public AccountDTO createAccount(Long userId, String accountType)
public List<AccountDTO> getUserAccounts(Long userId)
public AccountDTO getAccountByNumber(String accountNumber)
public void updateAccountBalance(Long accountId, BigDecimal newBalance)
```

#### 2.4 Implement TransactionService
**Location:** `src/main/java/com/bank/transaction/TransactionService.java`

**Key Methods:**
```java
public void recordTransaction(String transactionId, Long fromAccountId, Long toAccountId, String type, BigDecimal amount, String description)
public List<TransactionDTO> getAccountTransactions(Long accountId)
```

### Phase 3: Database Access Layer

#### 3.1 Create Repository Classes
Create these repository classes in their respective `db` packages:

- `UserRepository` - CRUD operations for users
- `AccountRepository` - CRUD operations for accounts  
- `TransactionRepository` - CRUD operations for transactions
- `LogRepository` - CRUD operations for logs

**Example Repository Pattern:**
```java
public class UserRepository {
    private DatabaseManager dbManager;
    
    public UserRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public UserDTO findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        // Use dbManager.query(sql) and convert results to UserDTO
    }
}
```

### Phase 4: Mappers

#### 4.1 Create Mapper Classes
**Location:** `src/main/java/com/bank/mapper/`

Create mappers to convert between database results and DTOs:
- `UserMapper`
- `AccountMapper`
- `TransactionMapper`

**Example Mapper:**
```java
public class UserMapper {
    public static UserDTO mapFromDatabase(Map<String, Object> row) {
        UserDTO user = new UserDTO();
        user.setId(((Number) row.get("id")).longValue());
        user.setUsername((String) row.get("username"));
        // ... map other fields
        return user;
    }
}
```

### Phase 5: Orchestrators

#### 5.1 TransactionOrchestrator
**Location:** `src/main/java/com/bank/orchestrator/TransactionOrchestrator.java`

Handles complex transaction workflows:
- Validates user authentication
- Checks account ownership
- Verifies sufficient funds
- Updates account balances
- Records transactions
- Logs activities

#### 5.2 FixedDepositOrchestrator
Handles Savings → Fixed Deposit transfers with special locking rules.

### Phase 6: Complete CLI Integration

Update the MenuDisplay class to call your services instead of showing TODO messages.

## Database Usage Examples

### Using the Database Layer
```java
// Get database instance
DatabaseManager db = DatabaseManager.getInstance();

// SELECT query
List<Map<String, Object>> users = db.query("SELECT * FROM users WHERE username = 'john'");

// INSERT query
List<Map<String, Object>> result = db.query(
    "INSERT INTO users (username, password_hash, full_name, email, phone) " +
    "VALUES ('john', 'hashedpass', 'John Doe', 'john@email.com', '1234567890')"
);
Long newUserId = (Long) result.get(0).get("generated_key");

// UPDATE query
db.query("UPDATE accounts SET balance = 1500.00 WHERE id = 1");
```

## Key Design Patterns

### 1. Singleton Pattern
- DatabaseManager uses singleton pattern
- Ensures single database connection

### 2. DTO Pattern
- Transfer data between layers
- Avoid exposing database entities

### 3. Repository Pattern
- Separate data access logic
- Easy to test and maintain

### 4. Service Layer Pattern
- Business logic in service classes
- Keep controllers/CLI thin

## Error Handling Best Practices

### 1. Use Custom Exceptions
```java
try {
    authService.login(username, password);
} catch (InvalidCredentialsException e) {
    menuDisplay.showError("Invalid username or password");
} catch (Exception e) {
    menuDisplay.showError("Login failed: " + e.getMessage());
}
```

### 2. Log All Important Actions
```java
// Log successful operations
logService.logAction(userId, "LOGIN_SUCCESS", "User logged in successfully");

// Log failures
logService.logAction(userId, "WITHDRAWAL_FAILED", "Insufficient funds: " + e.getMessage());
```

## Testing Tips

### 1. Unit Testing
```java
@Test
public void testCreateUser() {
    UserService userService = new UserService();
    UserDTO user = userService.createUser("testuser", "password", "Test User", "test@email.com", "1234567890");
    assertNotNull(user.getId());
    assertEquals("testuser", user.getUsername());
}
```

### 2. Integration Testing
Test complete workflows like login → create account → deposit → withdraw.

## Common Pitfalls to Avoid

1. **Don't put business logic in CLI classes** - Keep MenuDisplay thin
2. **Always validate input** - Check for null, empty strings, negative amounts
3. **Handle SQL exceptions** - Wrap in try-catch blocks
4. **Use BigDecimal for money** - Never use double/float for currency
5. **Close resources** - Though DatabaseManager handles this
6. **Validate account ownership** - Users should only access their own accounts

## Stretch Goals Implementation

### 1. Password Hashing with BCrypt
```java
import org.mindrot.jbcrypt.BCrypt;

public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
}

public boolean verifyPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
}
```

### 2. Configuration File
Create `application.properties`:
```properties
database.url=jdbc:sqlite:banking_app.db
interest.rate.savings=0.03
interest.rate.fd=0.05
account.limit.savings=5
account.limit.fd=3
```

## Submission Checklist

- [ ] All functional requirements implemented
- [ ] Custom exceptions used appropriately
- [ ] DTOs and Mappers implemented
- [ ] Orchestrators handle complex workflows
- [ ] Proper error handling and validation
- [ ] JavaDoc comments for public methods
- [ ] README with run instructions
- [ ] Code follows Java naming conventions
- [ ] Database operations work correctly
- [ ] CLI is user-friendly and robust

Good luck with your implementation! Remember to implement one feature at a time and test thoroughly.
