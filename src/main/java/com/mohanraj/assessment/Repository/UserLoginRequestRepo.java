package com.mohanraj.assessment.Repository;  // Package declaration for the Repository layer

import com.mohanraj.assessment.Model.UserLoginRequest;  // Importing the UserLoginRequest model
import org.springframework.data.mongodb.repository.MongoRepository;  // Importing the MongoRepository interface
import org.springframework.stereotype.Repository;  // Importing the Repository annotation

@Repository  // Annotation to indicate that this interface is a repository
public interface UserLoginRequestRepo extends MongoRepository<UserLoginRequest, Integer> {  // Interface declaration extending MongoRepository

    // Inherits standard CRUD operations from MongoRepository

    UserLoginRequest findByUsername(String username);  // Custom method to find UserLoginRequest by username
}
