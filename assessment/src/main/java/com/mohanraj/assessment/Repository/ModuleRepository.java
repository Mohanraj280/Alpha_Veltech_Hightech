package com.mohanraj.assessment.Repository;

import com.mohanraj.assessment.Model.Module;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends MongoRepository<Module, String> {
}

