package com.bank.services;
import com.bank.db.DatabaseManager;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.entity.UserEntity;
import com.bank.mapper.UserMapper;
import com.bank.util.ConsoleColor;
import com.bank.util.PasswordUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
            System.err.println(ConsoleColor.YELLOW+"Username cannot be empty."+ConsoleColor.RESET);
            return false;
        }

        if (userDao.existsByUsername(username)) {
            System.err.println(ConsoleColor.YELLOW+"Username already exists. Please choose another."+ConsoleColor.RESET);
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
            System.err.println(ConsoleColor.RED+"Error creating user: " + e.getMessage()+ConsoleColor.RESET);
            return false;
        }
    }



    /**
     * Displays a user's profile by username.
     */


    public void displayProfile(int user_id) throws Exception {
        UserEntity user = userDao.getUserById(user_id);
        if (user == null) {
            System.err.println(ConsoleColor.YELLOW+"User not found."+ConsoleColor.RESET);
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


    public boolean updateUserProfile(int id, String newFullName, String newEmail, String newPhone) throws Exception {
        UserEntity existingUser = userDao.getUserById(id);

        existingUser.setFullName(newFullName);
        existingUser.setEmail(newEmail);
        existingUser.setPhone(newPhone);
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        String formatted=sdf.format(ts);
        existingUser.setUpdatedAt(formatted);

        return userDao.updateUser(existingUser);
    }
}
