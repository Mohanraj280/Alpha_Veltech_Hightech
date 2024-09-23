package com.mohanraj.assessment.Service;

import com.mohanraj.assessment.Model.User;
import com.mohanraj.assessment.Model.UserLoginRequest;
import com.mohanraj.assessment.Repository.UserRepo;
import com.mohanraj.assessment.Repository.UserLoginRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserLoginRequestRepo userLogin;

    public boolean authenticateUser(String username, String email, String password) {
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByUsername(username));

        return userOptional.isPresent() &&
                userOptional.get().getEmail().equals(email) &&
                "123".equals(password);
    }

    public boolean authenticateAdmin(String username, String password) {
        Optional<UserLoginRequest> userLoginRequestOptional = Optional.ofNullable(userLogin.findByUsername(username));

        return userLoginRequestOptional.isPresent() &&
                userLoginRequestOptional.get().getPassword().equals(password);
    }
}
