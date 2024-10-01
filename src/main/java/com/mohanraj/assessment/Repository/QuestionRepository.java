package com.mohanraj.assessment.Repository;  // Package declaration for the Repository layer

import com.mohanraj.assessment.Model.Question;  // Importing the Question model
import org.springframework.data.mongodb.repository.MongoRepository;  // Importing the MongoRepository interface
import org.springframework.stereotype.Repository;  // Importing the Repository annotation

import java.util.List;  // Importing List for returning multiple Question entities

@Repository  // Annotation to indicate that this interface is a repository
public interface QuestionRepository extends MongoRepository<Question, String> {  // Interface definition extending MongoRepository

    // Inherits standard CRUD operations from MongoRepository

    List<Question> findAll();  // Custom method to retrieve all Question entities
}
