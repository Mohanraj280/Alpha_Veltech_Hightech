package com.mohanraj.assessment.Repository;

import com.mohanraj.assessment.Model.UserLoginRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRequestRepo extends MongoRepository<UserLoginRequest,Integer> {
    UserLoginRequest findByUsername(String username);
}
