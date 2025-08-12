package Orchestrators;
import java.sql.Timestamp;
import java.time.Instant;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.entity.UserEntity;
import com.bank.mapper.UserMapper;
import com.bank.util.PasswordUtil;


/**
 * Orchestrates user-related operations such as signup.
 * Responsible for coordinating between services, mappers, and DAO layers.
 */


public class UserOrchestrator {
   //here instead of dao create an instance of userservice so will call userservice from here
    private final userDao userDao;

    public UserOrchestrator(userDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Handles user signup.
     * Validates uniqueness of username, hashes the password,
     * prepares DTO and entity, and saves user to the database.
     *
     * @param username desired username
     * @param password plain text password
     * @param fullName user's full name
     * @param email user's email
     * @param phone user's phone number
     * @throws Exception if the username already exists or other errors occur
     */


    public void signup(String username, String password, String fullName, String email, String phone) throws Exception {

        // Check if username is unique

        if (userDao.existsByUsername(username)) {
            throw new Exception("Username already exists. Signup cancelled.");
        }


        // Hash password with PasswordUtil

        String passwordHash = PasswordUtil.hashPassword(password);

        // Generate unique ID and current timestamps

        long userId = generateUniqueId();
        Timestamp now = Timestamp.from(Instant.now());


        // Create UserDTO with user data

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername(username);
        userDTO.setFullName(fullName);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setCreatedAt(String.valueOf(now));
        userDTO.setUpdatedAt(String.valueOf(now));


        // Convert UserDTO to UserEntity

        UserEntity userEntity = UserMapper.UserDtoToUserEntity(userDTO);


        // Set password hash and timestamps on entity

        userEntity.setPasswordHash(passwordHash);
        userEntity.setCreatedAt(now);
        userEntity.setUpdatedAt(now);


        // Store user into DB

        userDao.createUser(userEntity); //call user services here and that will call the dao
    }                     //DO the logging


    // Method To Generate Unique id

    private long generateUniqueId() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }
}
