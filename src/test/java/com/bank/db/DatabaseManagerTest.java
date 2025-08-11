package com.bank.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Sample test class to demonstrate how to test the DatabaseManager.
 * This shows freshers how to write unit tests for their application.
 */
public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;
    
    @BeforeEach
    public void setUp() {
        // Use test database instance instead of regular instance
        dbManager = DatabaseManager.getTestInstance();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test data if needed
        try {
            dbManager.query("DELETE FROM transactions WHERE transaction_id LIKE 'TEST_%'");
            dbManager.query("DELETE FROM accounts WHERE account_number LIKE 'ACC%'");
            dbManager.query("DELETE FROM users WHERE username LIKE 'test_%'");
            dbManager.query("DELETE FROM logs WHERE action = 'TEST_ACTION'");
        } catch (SQLException e) {
            // Ignore cleanup errors in tests
        }
    }
    
    @AfterAll
    public static void cleanupTestDatabase() {
        // Reset the singleton instance to ensure clean state
        DatabaseManager.resetInstance();
        
        // Optionally delete the test database file
        try {
            java.io.File testDbFile = new java.io.File("banking_app_test.db");
            if (testDbFile.exists()) {
                testDbFile.delete();
                System.out.println("Test database file deleted successfully.");
            }
        } catch (Exception e) {
            System.err.println("Could not delete test database file: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseConnection() {
        assertTrue(dbManager.isConnected(), "Database should be connected");
    }
    
    @Test
    public void testCreateAndRetrieveUser() throws SQLException {
        // Insert a test user
        String insertSql = "INSERT INTO users (username, password_hash, full_name, email, phone) " +
                          "VALUES ('test_user', 'hashed_password', 'Test User', 'test@example.com', '1234567890')";
        
        List<Map<String, Object>> insertResult = dbManager.query(insertSql);
        assertFalse(insertResult.isEmpty(), "Insert should return result");
        
        Long userId = (Long) insertResult.get(0).get("generated_key");
        assertNotNull(userId, "Should get generated user ID");
        
        // Retrieve the user
        String selectSql = "SELECT * FROM users WHERE id = " + userId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertFalse(selectResult.isEmpty(), "Should find the inserted user");
        Map<String, Object> user = selectResult.get(0);
        assertEquals("test_user", user.get("username"));
        assertEquals("Test User", user.get("full_name"));
        assertEquals("test@example.com", user.get("email"));
    }
    
    @Test
    public void testCreateAccount() throws SQLException {
        // First create a user
        String userSql = "INSERT INTO users (username, password_hash, full_name, email, phone) " +
                        "VALUES ('test_account_user', 'hashed_password', 'Account Test User', 'account@example.com', '9876543210')";
        
        List<Map<String, Object>> userResult = dbManager.query(userSql);
        Long userId = (Long) userResult.get(0).get("generated_key");
        
        // Create an account for the user
        String accountSql = "INSERT INTO accounts (account_number, user_id, account_type, balance) " +
                           "VALUES ('ACC001', " + userId + ", 'SAVINGS', 1000.00)";
        
        List<Map<String, Object>> accountResult = dbManager.query(accountSql);
        assertFalse(accountResult.isEmpty(), "Account creation should return result");
        
        Long accountId = (Long) accountResult.get(0).get("generated_key");
        assertNotNull(accountId, "Should get generated account ID");
        
        // Verify the account
        String selectSql = "SELECT * FROM accounts WHERE id = " + accountId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertFalse(selectResult.isEmpty(), "Should find the created account");
        Map<String, Object> account = selectResult.get(0);
        assertEquals("ACC001", account.get("account_number"));
        assertEquals("SAVINGS", account.get("account_type"));
        assertEquals(1000.0, ((Number) account.get("balance")).doubleValue(), 0.01);
    }
    
    @Test
    public void testUpdateAccountBalance() throws SQLException {
        // Create user and account first
        String userSql = "INSERT INTO users (username, password_hash, full_name, email, phone) " +
                        "VALUES ('test_balance_user', 'hashed_password', 'Balance Test User', 'balance@example.com', '5555555555')";
        
        List<Map<String, Object>> userResult = dbManager.query(userSql);
        Long userId = (Long) userResult.get(0).get("generated_key");
        
        String accountSql = "INSERT INTO accounts (account_number, user_id, account_type, balance) " +
                           "VALUES ('ACC002', " + userId + ", 'SAVINGS', 500.00)";
        
        List<Map<String, Object>> accountResult = dbManager.query(accountSql);
        Long accountId = (Long) accountResult.get(0).get("generated_key");
        
        // Update the balance
        String updateSql = "UPDATE accounts SET balance = 750.00 WHERE id = " + accountId;
        List<Map<String, Object>> updateResult = dbManager.query(updateSql);
        
        assertEquals(1, ((Number) updateResult.get(0).get("affected_rows")).intValue(), 
                    "Should update exactly one row");
        
        // Verify the update
        String selectSql = "SELECT balance FROM accounts WHERE id = " + accountId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertEquals(750.0, ((Number) selectResult.get(0).get("balance")).doubleValue(), 0.01,
                    "Balance should be updated to 750.00");
    }
    
    @Test
    public void testTransactionLogging() throws SQLException {
        // Test logging functionality
        String logSql = "INSERT INTO logs (user_id, action, details, status) " +
                       "VALUES (1, 'TEST_ACTION', 'This is a test log entry', 'SUCCESS')";
        
        List<Map<String, Object>> logResult = dbManager.query(logSql);
        assertFalse(logResult.isEmpty(), "Log insertion should return result");
        
        Long logId = (Long) logResult.get(0).get("generated_key");
        assertNotNull(logId, "Should get generated log ID");
        
        // Verify the log entry
        String selectSql = "SELECT * FROM logs WHERE id = " + logId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertFalse(selectResult.isEmpty(), "Should find the log entry");
        Map<String, Object> log = selectResult.get(0);
        assertEquals("TEST_ACTION", log.get("action"));
        assertEquals("This is a test log entry", log.get("details"));
        assertEquals("SUCCESS", log.get("status"));
    }
}
