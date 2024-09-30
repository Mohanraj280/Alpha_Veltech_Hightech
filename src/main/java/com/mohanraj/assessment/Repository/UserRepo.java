package com.mohanraj.assessment.Repository;  // Package declaration for the Repository layer

import com.mohanraj.assessment.Model.User;  // Importing the User model
import org.springframework.data.mongodb.repository.MongoRepository;  // Importing the MongoRepository interface
import org.springframework.stereotype.Repository;  // Importing the Repository annotation

@Repository  // Annotation to indicate that this interface is a repository
public interface UserRepo extends MongoRepository<User, String> {  // Interface declaration extending MongoRepository

    // Inherits standard CRUD operations from MongoRepository

    User findByUsername(String username);  // Custom method to find User by username

    User findByEmail(String email);  // Custom method to find User by email
}
