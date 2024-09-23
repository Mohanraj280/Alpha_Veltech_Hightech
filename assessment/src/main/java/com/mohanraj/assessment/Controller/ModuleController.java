package com.mohanraj.assessment.Controller;


import com.mohanraj.assessment.Model.Module;
import com.mohanraj.assessment.Model.Question;
import com.mohanraj.assessment.Repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assessment")
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepository;


    @PostMapping("/create")
    public ResponseEntity<String> createAssessment(@RequestBody Module module)
    {
        moduleRepository.save(module);
        return new ResponseEntity<>("Assessment created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/api/modules")
    public List<Module> showModules() {
        // Return the list of modules as JSON
        return moduleRepository.findAll();
    }

    @GetMapping("/{moduleId}/questions")
    public ResponseEntity<List<Question>> getModuleQuestions(@PathVariable String moduleId) {

        Optional<Module> module = moduleRepository.findById(moduleId);
        if (module.isPresent() && !module.get().getQuestions().isEmpty()) {
            return ResponseEntity.ok(module.get().getQuestions());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList()); // or handle error properly
    }

}
