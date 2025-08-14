package com.bank.db;
import com.bank.mapper.UserMapper;
import com.bank.entity.UserEntity;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 * Data Access Object for interacting with the USERS table in the database.
 * This class provides CRUD operations and utility functions related to user data.
 */


public class userDao {

    /**
     * Constructor for userDao.
     *
     * @param dm DatabaseManager instance for executing queries
     */


    public userDao(DatabaseManager dm){
        this.dm = dm;
    }

    private final DatabaseManager dm;





    /**
     * Inserts a new user record into the USERS table.
     *
     * @param user UserEntity containing user information to be saved
     * @throws Exception if database operation fails
     */


    public boolean createUser(UserEntity user) throws Exception {
        String sql = String.format(
                "INSERT INTO users (username, password_hash, full_name, email, phone) " +
                        "VALUES ( '%s', '%s', '%s', '%s', '%s')",
//                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone()
//                user.getCreatedAt(),
//                user.getUpdatedAt()
        );
        dm.query(sql);
        return true;
    }


    /**
     * Retrieves a user by their unique ID.
     *
     * @param id User's unique ID
     * @return UserEntity if found, or null if no such user exists
     * @throws Exception if database operation fails
     */


    public UserEntity getUserById(int id) throws Exception {
        String sql = String.format(
                "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS WHERE id = '%d'",
                id
        );

        List<Map<String, Object>> rows = dm.query(sql);
        if (!rows.isEmpty()) {
            return UserMapper.mapToUserEntity(rows.get(0));
        }
        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The unique username to look for
     * @return UserEntity if found, or null if user does not exist
     * @throws Exception if database operation fails
     */


    public UserEntity getUserByUsername(String username) throws Exception {
        String sql = String.format(
                "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS WHERE username = '%s'",
                username
        );
        List<Map<String, Object>> rows = dm.query(sql);
        if (!rows.isEmpty()) {
            return UserMapper.mapToUserEntity(rows.get(0));
        }
        return null;
    }


    /**
     * Retrieves all users from the USERS table.
     *
     * @return List of UserEntity objects
     * @throws Exception if database operation fails
     */


    public List<UserEntity> getAllUsers() throws Exception {
        String sql = "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS";
        List<Map<String, Object>> rows = dm.query(sql);
        return UserMapper.mapToUserEntityList(rows);
    }


    /**
     * Updates an existing user's information in the USERS table.
     *
     * @param user UserEntity with updated data
     * @throws Exception if database operation fails
     */


    public boolean updateUser(UserEntity user) throws Exception {
        String sql = String.format(
                "UPDATE USERS SET username = '%s', password_hash = '%s', full_name = '%s', email = '%s', phone = '%s', updated_at = '%s' " +
                        "WHERE id = '%d'",
                user.getUsername(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getUpdatedAt(),
                user.getId()
        );
        dm.query(sql);
        return true;
    }


    /**
     * Deletes a user from the USERS table by ID.
     *
     * @param id User's unique ID
     * @throws Exception if database operation fails
     */


    public void deleteUser(String id) throws Exception {
        String sql = String.format("DELETE FROM USERS WHERE id = '%s'", id);
        dm.query(sql);
    }


    /**
     * Checks if a username already exists in the USERS table.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     * @throws Exception if database operation fails
     */


    public boolean existsByUsername(String username) throws Exception {
        String sql = String.format("SELECT 1 FROM USERS WHERE username = '%s' LIMIT 1", username);
        List<Map<String, Object>> rows = dm.query(sql);
        return !rows.isEmpty();
    }
}