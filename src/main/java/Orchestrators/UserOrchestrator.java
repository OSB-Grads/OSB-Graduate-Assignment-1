package Orchestrators;
import java.util.UUID;
import com.bank.db.userDao;
import com.bank.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.time.Instant;

public class UserOrchestrator {

    private final userDao userDao;

    public UserOrchestrator(userDao userDao) {
        this.userDao = userDao;
    }

    /*
      Throws Exception if username is already taken.
     */
    public void signup(String username, String password, String fullName, String email, String phone) throws Exception {
        // Check if username is unique
        if (userDao.existsByUsername(username)) {
            throw new Exception("Username already exists. Signup cancelled.");
        }

        // Hash password with BCrypt
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create UserEntity and set fields
        UserEntity user = new UserEntity();
        user.setId(generateUniqueId());
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Store user into DB
        userDao.createUser(user);
    }



     //Method To Generate Unique id
    private long generateUniqueId() {
        UUID uuid = UUID.randomUUID();

        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

}
