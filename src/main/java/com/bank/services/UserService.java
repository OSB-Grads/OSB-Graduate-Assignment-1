package com.bank.services;
import com.bank.db.DatabaseManager;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.entity.UserEntity;
import com.bank.mapper.UserMapper;
import com.bank.util.PasswordUtil;
import java.sql.Timestamp;

import static com.bank.mapper.UserMapper.UserDtoToUserEntity;


public class UserService {

    private final userDao userDao ;

    public UserService(DatabaseManager dm) {
        this.userDao = new userDao(dm);
    }


    /**
     * Creates a new user after validating input and hashing password.
     */

    public boolean createUser(String username, String fullName, String email, String phone, String hashedPassword) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty.");
            return false;
        }

        if (userDao.existsByUsername(username)) {
            System.err.println("Username already exists. Please choose another.");
            return false;
        }

        try {
            // Step 1: Create DTO
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setFullName(fullName);
            userDTO.setEmail(email);
            userDTO.setPhone(phone);
//            userDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//            userDTO.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            // Step 2: Map DTO to Entity with hashed password
            UserEntity userEntity = UserMapper.UserDtoToUserEntity(userDTO, hashedPassword);
//            userEntity.setCreatedAt(userDTO.getCreatedAt());
//            userEntity.setUpdatedAt(userDTO.getUpdatedAt());

            // Step 3: Save to DB
            return userDao.createUser(userEntity);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }



    /**
     * Displays a user's profile by username.
     */


    public void displayProfile(int user_id) throws Exception {
        UserEntity user = userDao.getUserById(user_id);
        if (user == null) {
            System.err.println("User not found.");
            return;
        }

        System.out.println("===== User Profile =====");
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("========================");
    }


    /**
     * Verifies username  before allowing updates.
     */


    public boolean verifyAndUpdateUser(String username, String plainPassword, String newFullName, String newEmail, String newPhone) throws Exception {
        UserEntity existingUser = userDao.getUserByUsername(username);

        if (existingUser == null) {
            System.err.println("User not found.");
            return false;
        }


        existingUser.setFullName(newFullName);
        existingUser.setEmail(newEmail);
        existingUser.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        existingUser.setPhone(newPhone);
        existingUser.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        return userDao.updateUser(existingUser);
    }
}
