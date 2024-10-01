package com.mohanraj.assessment.Service;  // Package declaration for the Service layer

import com.mohanraj.assessment.Model.Module;  // Importing the Module model
import com.mohanraj.assessment.Repository.ModuleRepository;  // Importing the ModuleRepository interface
import org.springframework.beans.factory.annotation.Autowired;  // Importing the Autowired annotation
import org.springframework.stereotype.Service;  // Importing the Service annotation

import java.util.List;  // Importing List for returning multiple modules
import java.util.Optional;  // Importing Optional for optional values

@Service  // Annotation to indicate that this class is a service component
public class ModuleService {

    @Autowired  // Automatically injects the ModuleRepository bean
    private ModuleRepository moduleRepository;

    // Get all modules from the database
    public List<Module> getAllModules() {
        return moduleRepository.findAll();  // Returns a list of all modules from the repository
    }

    // Get a module by its ID
    public Optional<Module> getModuleById(String moduleId) {
        return moduleRepository.findById(moduleId);  // Returns an Optional containing the module if found
    }

    // Save a new module or update an existing one
    public Module saveModule(Module module) {
        return moduleRepository.save(module);  // Saves the module and returns the saved entity
    }

    // Delete a module by its ID
    public void deleteModuleById(String moduleId) {
        moduleRepository.deleteById(moduleId);  // Deletes the module identified by the given ID
    }
}
