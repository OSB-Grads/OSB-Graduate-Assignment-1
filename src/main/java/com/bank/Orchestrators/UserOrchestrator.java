package com.bank.Orchestrators;
import com.bank.db.LogDAO;
import com.bank.dto.UserDTO;
import com.bank.services.AuthService;
import com.bank.services.UserService;
import com.bank.services.LogService;


/**
 * Orchestrates user-related operations such as signup and profile updates.
 * Responsible for coordinating between services, mappers, and DAO layers.
 */

public class UserOrchestrator {

    private final UserService userService;
    private final AuthService authService;
    public UserOrchestrator(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService= authService;
    }

    /**
     * Handles user signup.
     * Validates uniqueness of username, hashes the password,
     * prepares DTO and entity, and saves user to the database.
     *
     * @param username desired username
     * @param password plain text password
     * @param fullName user's full name
     * @param email    user's email
     * @param phone    user's phone number
     * @throws Exception if the username already exists or other errors occur
     */


    public void signup(int userId, String username, String password, String fullName, String email, String phone) throws Exception {

        /* This Method Calls AuthService To Authenticate The UserName And Password */

        String[] authResult = AuthService.SignInUser(username, password);

        String hashedPassword = authResult[1];

        userService.createUser(username, fullName, email, phone, hashedPassword);


        LogService.logintoDB(
                userId,
                LogDAO.Action.PROFILE_MANAGEMENT,  // ← enum, not string
                "Created user",
                "LocalDesktop",
                LogDAO.Status.SUCCESS               // ← enum, not string
        );

    }


    /**
     * Displays the user profile.
     *
     * @param username username of the user to display
     * @throws Exception if user not found
     */


    public void displayProfile(int UserId) throws Exception {
        userService.displayProfile(UserId);
    }



    /**
     * Updates the user's details after verifying credentials.
     *
     * @param username existing username
     * @param password plain text password for verification
     * @param updatedDTO updated details
     * @throws Exception if verification fails
     */


    public void updateUserDetails(int id, UserDTO updatedDTO) throws Exception {
        userService.updateUserProfile(
                id,
                updatedDTO.getFullName(),
                updatedDTO.getEmail(),
                updatedDTO.getPhone()
        );
    }
}