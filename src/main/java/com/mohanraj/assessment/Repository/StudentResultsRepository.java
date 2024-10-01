package com.mohanraj.assessment.Repository;

import com.mohanraj.assessment.Model.StudentResults;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marks this interface as a Spring Data repository
public interface StudentResultsRepository extends MongoRepository<StudentResults, String> {

    // Custom query method to find results by username
    List<StudentResults> findByUsername(String username);

    // Custom query method to find results by email
    List<StudentResults> findByEmail(String email);

    // You can add more query methods if needed
}
