package com.mohanraj.assessment.Service;

import com.mohanraj.assessment.Model.Module;
import com.mohanraj.assessment.Repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    // Get all modules from the database
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    // Get a module by its ID
    public Optional<Module> getModuleById(String moduleId) {
        return moduleRepository.findById(moduleId);
    }

    // Save a new module or update an existing one
    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    // Delete a module by its ID
    public void deleteModuleById(String moduleId) {
        moduleRepository.deleteById(moduleId);
    }
}
