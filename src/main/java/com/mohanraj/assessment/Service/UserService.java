package com.mohanraj.assessment.Service;  // Package declaration for the Service layer

import com.mohanraj.assessment.Model.User;  // Importing the User model
import com.mohanraj.assessment.Model.UserLoginRequest;  // Importing the UserLoginRequest model
import com.mohanraj.assessment.Repository.UserRepo;  // Importing the UserRepo interface
import com.mohanraj.assessment.Repository.UserLoginRequestRepo;  // Importing the UserLoginRequestRepo interface
import org.springframework.beans.factory.annotation.Autowired;  // Importing the Autowired annotation
import org.springframework.stereotype.Service;  // Importing the Service annotation

import java.util.Optional;  // Importing Optional for safe retrieval of user objects

@Service  // Annotation to indicate that this class is a service component
public class UserService {

    @Autowired  // Automatically injects the UserRepo bean
    private UserRepo userRepo;  // Repository for User data

    @Autowired  // Automatically injects the UserLoginRequestRepo bean
    private UserLoginRequestRepo userLogin;  // Repository for user login requests

    // Method to authenticate a user based on username, email, and password
    public boolean authenticateUser(String username, String email, String password) {
        // Find user by username and wrap it in an Optional
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByUsername(username));

        // Check if user exists and verify email and password
        return userOptional.isPresent() &&
                userOptional.get().getEmail().equals(email) &&
                "123".equals(password); // Replace with a proper password validation mechanism
    }

    // Method to authenticate an admin based on username and password
    public boolean authenticateAdmin(String username, String password) {
        // Find user login request by username and wrap it in an Optional
        Optional<UserLoginRequest> userLoginRequestOptional = Optional.ofNullable(userLogin.findByUsername(username));

        // Check if user login request exists and verify password
        return userLoginRequestOptional.isPresent() &&
                userLoginRequestOptional.get().getPassword().equals(password); // Replace with a proper password validation mechanism
    }
}
