package com.mohanraj.assessment.Repository;  // Package declaration for the Repository layer

import com.mohanraj.assessment.Model.Module;  // Importing the Module model
import org.springframework.data.mongodb.repository.MongoRepository;  // Importing the MongoRepository interface
import org.springframework.stereotype.Repository;  // Importing the Repository annotation

@Repository  // Annotation to indicate that this interface is a repository
public interface ModuleRepository extends MongoRepository<Module, String> {  // Interface definition extending MongoRepository

    // No additional methods are needed here;
    // MongoRepository provides CRUD operations for the Module entity.

    // The first parameter is the entity type (Module),
    // and the second parameter is the type of the entity's ID (String).
}
