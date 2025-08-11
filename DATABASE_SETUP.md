# Database Configuration

This project now uses separate databases for the application and tests to ensure data isolation and prevent test data from interfering with application data.

## Database Files

- **Application Database**: `banking_app.db` - Used by the main application
- **Test Database**: `banking_app_test.db` - Used during testing (automatically created and cleaned up)

## How It Works

### DatabaseManager Class

The `DatabaseManager` class has been modified to support configurable database URLs:

- `getInstance()` - Returns the singleton instance using the main application database
- `getTestInstance()` - Returns a singleton instance configured for testing with the test database
- `resetInstance()` - Resets the singleton (useful for testing cleanup)

### Test Configuration

The test class `DatabaseManagerTest` uses the following setup:

1. **@BeforeEach**: Calls `DatabaseManager.getTestInstance()` to ensure tests use the test database
2. **@AfterEach**: Cleans up test data from the current test
3. **@AfterAll**: Resets the singleton instance and deletes the test database file

## Benefits

1. **Data Isolation**: Tests don't interfere with application data
2. **Clean Testing Environment**: Each test run starts with a fresh database
3. **No Manual Cleanup**: Test database is automatically created and destroyed
4. **Backward Compatibility**: Existing application code continues to work unchanged

## Usage

### For Application Code
```java
DatabaseManager dbManager = DatabaseManager.getInstance();
// Uses banking_app.db
```

### For Test Code
```java
DatabaseManager dbManager = DatabaseManager.getTestInstance();
// Uses banking_app_test.db
```

## Running Tests

```bash
mvn test
```

Tests will automatically:
1. Create a test database (`banking_app_test.db`)
2. Run all tests using the test database
3. Clean up and delete the test database after completion

## Running the Application

```bash
mvn compile exec:java
```

The application will use the main database (`banking_app.db`) and will not be affected by test data.
