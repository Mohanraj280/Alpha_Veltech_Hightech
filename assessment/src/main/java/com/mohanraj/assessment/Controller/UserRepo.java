package com.mohanraj.assessment.Controller;

import com.mohanraj.assessment.Controller.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
}
