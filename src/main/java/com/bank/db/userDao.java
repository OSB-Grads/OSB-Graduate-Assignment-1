package com.bank.db;

import com.bank.entity.UserEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class userDao {

    private final DatabaseManager dm;

    public userDao(DatabaseManager dm) {
        this.dm = dm;
    }



    // CREATE
    public void createUser(UserEntity user) throws Exception {
        String sql = String.format(
                "INSERT INTO USERS (id, username, password_hash, full_name, email, phone, created_at, updated_at) " +
                        "VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        dm.query(sql);
    }

    // READ - By ID
    public UserEntity getUserById(String id) throws Exception {
        String sql = String.format(
                "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS WHERE id = '%s'",
                id
        );
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    // READ - By Username
    public UserEntity getUserByUsername(String username) throws Exception {
        String sql = String.format(
                "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS WHERE username = '%s'",
                username
        );
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    // READ - All
    public List<UserEntity> getAllUsers() throws Exception {
        List<UserEntity> users = new ArrayList<>();
        String sql = "SELECT id, username, password_hash, full_name, email, phone, created_at, updated_at FROM USERS";
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    // UPDATE
    public void updateUser(UserEntity user) throws Exception {
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
    }

    // DELETE
    public void deleteUser(String id) throws Exception {
        String sql = String.format("DELETE FROM USERS WHERE id = '%s'", id);
        dm.query(sql);
    }

    // EXISTS
    public boolean existsByUsername(String username) throws Exception {
        String sql = String.format("SELECT 1 FROM USERS WHERE username = '%s' LIMIT 1", username);
        try (ResultSet rs = (ResultSet) dm.query(sql)) {
            return rs.next();
        }
    }

    // Mapper
    private UserEntity mapResultSetToUser(ResultSet rs) throws Exception {
        UserEntity user = new UserEntity();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}