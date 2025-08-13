package com.bank.Orchestrators;
import com.bank.dto.UserDTO;
import com.bank.services.UserService; // Import your service layer

/**
 * Orchestrates user-related operations such as signup and profile updates.
 * Responsible for coordinating between services, mappers, and DAO layers.
 */

public class UserOrchestrator {

    private final UserService userService;

    public UserOrchestrator(UserService userService) {
        this.userService = userService;
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


    public void signup(String username, String password, String fullName, String email, String phone) throws Exception {


        userService.createUser(username, fullName, email, phone, password);
    }


    /**
     * Displays the user profile.
     *
     * @param username username of the user to display
     * @throws Exception if user not found
     */


    public void displayProfile(String username) throws Exception {
        userService.displayProfile(username);
    }



    /**
     * Updates the user's details after verifying credentials.
     *
     * @param username existing username
     * @param password plain text password for verification
     * @param updatedDTO updated details
     * @throws Exception if verification fails
     */


    public void updateUserDetails(String username, String password, UserDTO updatedDTO) throws Exception {
        userService.verifyAndUpdateUser(
                username,
                password,
                updatedDTO.getFullName(),
                updatedDTO.getEmail(),
                updatedDTO.getPhone()
        );

    }
}