package com.bank.db;

import java.sql.*;
import java.util.*;

/**
 * Simple database layer that provides a single query function.
 * This class handles all database connections and operations.
 * 
 * Usage: DatabaseManager.getInstance().query("SELECT * FROM users")
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    
    // Database configuration
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:banking_app.db";
    private static final String TEST_DB_URL = "jdbc:sqlite:banking_app_test.db";
    private String dbUrl;
    
    private DatabaseManager() {
        this(DEFAULT_DB_URL);
    }
    
    private DatabaseManager(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }
    
    /**
     * Get singleton instance of DatabaseManager
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Get test instance of DatabaseManager with test database
     * @return DatabaseManager instance configured for testing
     */
    public static synchronized DatabaseManager getTestInstance() {
        if (instance != null) {
            instance.close();
        }
        instance = new DatabaseManager(TEST_DB_URL);
        return instance;
    }
    
    /**
     * Reset the singleton instance (useful for testing)
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
    
    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection
            connection = DriverManager.getConnection(dbUrl);
            
            // Create tables if they don't exist
            createTables();
            
            System.out.println("Database initialized successfully.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please add sqlite-jdbc dependency to your project.");
            System.err.println("Add this to your pom.xml dependencies:");
            System.err.println("<dependency>");
            System.err.println("    <groupId>org.xerial</groupId>");
            System.err.println("    <artifactId>sqlite-jdbc</artifactId>");
            System.err.println("    <version>3.42.0.0</version>");
            System.err.println("</dependency>");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Create necessary tables for the banking application
     */
    private void createTables() {
        String[] createTableQueries = {
            // Users table
            "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password_hash VARCHAR(255) NOT NULL, " +
            "full_name VARCHAR(100) NOT NULL, " +
            "email VARCHAR(100), " +
            "phone VARCHAR(20), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")",
            
            // Accounts table
            "CREATE TABLE IF NOT EXISTS accounts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "account_number VARCHAR(20) UNIQUE NOT NULL, " +
            "user_id INTEGER NOT NULL, " +
            "account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('SAVINGS', 'FIXED_DEPOSIT')), " +
            "balance DECIMAL(15,2) DEFAULT 0.00, " +
            "is_locked BOOLEAN DEFAULT FALSE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (user_id) REFERENCES users(id)" +
            ")",
            
            // Transactions table
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "transaction_id VARCHAR(50) UNIQUE NOT NULL, " +
            "from_account_id INTEGER, " +
            "to_account_id INTEGER, " +
            "transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')), " +
            "amount DECIMAL(15,2) NOT NULL, " +
            "description TEXT, " +
            "status VARCHAR(20) DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (from_account_id) REFERENCES accounts(id), " +
            "FOREIGN KEY (to_account_id) REFERENCES accounts(id)" +
            ")",
            
            // Logs table
            "CREATE TABLE IF NOT EXISTS logs (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER, " +
            "action VARCHAR(100) NOT NULL, " +
            "details TEXT, " +
            "ip_address VARCHAR(45), " +
            "status VARCHAR(20) DEFAULT 'SUCCESS' CHECK (status IN ('SUCCESS', 'FAILURE', 'ERROR')), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (user_id) REFERENCES users(id)" +
            ")"
        };
        
        for (String query : createTableQueries) {
            try {
                query(query);
            } catch (Exception e) {
                System.err.println("Failed to create table: " + e.getMessage());
            }
        }
    }
    
    /**
     * Execute a SQL query and return results as a list of maps.
     * Each map represents a row with column names as keys.
     * 
     * @param sql The SQL query to execute
     * @return List of rows, where each row is a Map<String, Object>
     * @throws SQLException if query execution fails
     */
    public List<Map<String, Object>> query(String sql) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement statement = connection.createStatement()) {
            
            // Check if it's a SELECT query (returns ResultSet)
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = resultSet.getObject(i);
                            row.put(columnName, value);
                        }
                        results.add(row);
                    }
                }
            } else {
                // For INSERT, UPDATE, DELETE queries
                int affectedRows = statement.executeUpdate(sql);
                Map<String, Object> result = new HashMap<>();
                result.put("affected_rows", affectedRows);
                
                // For INSERT queries, try to get the generated key
                if (sql.trim().toUpperCase().startsWith("INSERT")) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            result.put("generated_key", generatedKeys.getLong(1));
                        }
                    }
                }
                
                results.add(result);
            }
        }
        
        return results;
    }
    
    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Check if database connection is valid
     * @return true if connection is valid, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
