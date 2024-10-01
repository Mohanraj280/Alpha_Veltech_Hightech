package com.mohanraj.assessment.Controller;  // Package declaration for the ModuleController

import com.mohanraj.assessment.Model.Module;  // Importing the Module model class
import com.mohanraj.assessment.Model.Question;  // Importing the Question model class
import com.mohanraj.assessment.Repository.ModuleRepository;  // Importing the ModuleRepository interface for database operations
import jakarta.servlet.http.HttpSession;  // Importing HttpSession for managing user sessions
import org.springframework.beans.factory.annotation.Autowired;  // Importing Autowired annotation for dependency injection
import org.springframework.http.HttpStatus;  // Importing HttpStatus for HTTP response status codes
import org.springframework.http.ResponseEntity;  // Importing ResponseEntity for building HTTP responses
import org.springframework.web.bind.annotation.*;  // Importing annotations for RESTful web services

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController  // Annotation to indicate that this class is a REST controller
@RequestMapping("/api/assessment")  // Base URL mapping for all endpoints in this controller
public class ModuleController {  // Class definition for ModuleController

    @Autowired  // Annotation for dependency injection of ModuleRepository
    private ModuleRepository moduleRepository;  // Instance of ModuleRepository for database operations

    @PostMapping("/create")  // Endpoint to handle POST requests for creating assessments
    public ResponseEntity<String> createAssessment(@RequestBody Module module) {  // Accepting a Module object in the request body
        moduleRepository.save(module);  // Save the new module to the database
        return new ResponseEntity<>("Assessment created successfully", HttpStatus.CREATED);  // Return success message with 201 Created status
    }

    @GetMapping("/api/modules")  // Endpoint to handle GET requests for retrieving modules
    public ResponseEntity<?> showModules(HttpSession session) {  // Accepting HttpSession to check user authentication
        String username = (String) session.getAttribute("username");  // Retrieve username from the session
        String email = (String) session.getAttribute("email");  // Retrieve email from the session
        System.out.println(username + " " + email);  // Log the username and email to the console
        if (username == null || email == null) {  // Check if the session attributes are missing
            // If session attributes are missing, return a 403 Forbidden response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");  // Return forbidden access message
        }

        // Return the list of modules as JSON
        List<Module> modules = moduleRepository.findAll();  // Retrieve all modules from the database
        return ResponseEntity.ok(modules);  // Return modules with 200 OK status
    }

    @GetMapping("/adminApi/modules")  // Endpoint for admin to retrieve modules
    public ResponseEntity<?> showAdminModules(HttpSession session) {  // Accepting HttpSession to check admin authentication
        String username = (String) session.getAttribute("username");  // Retrieve username from the session
        System.out.println(username);  // Log the username to the console
        if (username == null) {  // Check if the session attribute is missing
            // If session attribute is missing, return a 403 Forbidden response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");  // Return forbidden access message
        }

        // Return the list of modules as JSON
        List<Module> modules = moduleRepository.findAll();  // Retrieve all modules from the database
        return ResponseEntity.ok(modules);  // Return modules with 200 OK status
    }

    @GetMapping("/{moduleId}/questions")  // Endpoint to retrieve questions for a specific module
    public ResponseEntity<List<Question>> getModuleQuestions(@PathVariable String moduleId, HttpSession session) {  // Accepting module ID and HttpSession

        // Check if the user is authenticated by verifying session attributes
        String username = (String) session.getAttribute("username");  // Retrieve username from the session
        String email = (String) session.getAttribute("email");  // Retrieve email from the session

        if (username == null || email == null) {  // Check if the session attributes are missing
            // If session is invalid, return a 403 Forbidden status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());  // Return forbidden access with empty list
        }

        // Find the module by its ID
        Optional<Module> module = moduleRepository.findById(moduleId);  // Retrieve the module from the database by ID

        // If module exists and has questions, return them
        if (module.isPresent() && !module.get().getQuestions().isEmpty()) {  // Check if the module exists and has questions
            return ResponseEntity.ok(module.get().getQuestions());  // Return questions with 200 OK status
        }

        // If module is not found or has no questions, return 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());  // Return not found status with empty list
    }


    @GetMapping("/{moduleId}")  // Endpoint to retrieve questions for a specific module
    public ResponseEntity<Module> getModuleQuestionsForUpdate(@PathVariable String moduleId, HttpSession session) {  // Accepting module ID and HttpSession

        // Check if the user is authenticated by verifying session attributes
        String username = (String) session.getAttribute("username");  // Retrieve username from the session

        if (username == null) {  // Check if the session attributes are missing
            // If session is invalid, return a 403 Forbidden status
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);  // Return forbidden access with empty list
        }

         // Retrieve the module from the database by ID
        Optional<Module> module = moduleRepository.findById(moduleId);  // Fetch the module by ID

        // If the module exists, return the entire module with its details and questions
        if (module.isPresent()) {
            return ResponseEntity.ok(module.get());  // Return the full module object with 200 OK status
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // If module not found, return 404 status
        } // Return not found status with empty list
    }


    @PutMapping("/modules/{moduleId}")
    public ResponseEntity<Module> updateModule(
            @PathVariable String moduleId, @RequestBody Module updatedModuleDetails) {

        // Find the existing module by ID
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (!optionalModule.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if module not found
        }

        Module existingModule = optionalModule.get();

        // Update module details
        existingModule.setModuleName(updatedModuleDetails.getModuleName());
        existingModule.setModuleDescription(updatedModuleDetails.getModuleDescription());

        // Update questions (this part is critical for keeping question consistency)
        List<Question> updatedQuestions = updatedModuleDetails.getQuestions();
        if (updatedQuestions != null) {
            // Use a map to track the existing questions by ID
            Map<String, Question> existingQuestionsMap = existingModule.getQuestions()
                    .stream()
                    .collect(Collectors.toMap(Question::getId, Function.identity()));

            // Create a new list to store the final set of questions
            List<Question> finalQuestionsList = new ArrayList<>();

            for (Question updatedQuestion : updatedQuestions) {
                if (updatedQuestion.getId() != null && existingQuestionsMap.containsKey(updatedQuestion.getId())) {
                    // Update the existing question
                    Question existingQuestion = existingQuestionsMap.get(updatedQuestion.getId());
                    existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
                    existingQuestion.setOptions(updatedQuestion.getOptions());
                    existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                    finalQuestionsList.add(existingQuestion);
                } else {
                    // This is a new question (no ID or not found in existing)
                    finalQuestionsList.add(updatedQuestion);
                }
            }

            // Remove any questions that were in the original module but are not in the updated list
            existingModule.getQuestions().clear();  // Clear existing questions
            existingModule.setQuestions(finalQuestionsList);  // Add the final updated list of questions
        }

        // Save the updated module
        Module savedModule = moduleRepository.save(existingModule);

        // Return the updated module
        return ResponseEntity.ok(savedModule);
    }

    @DeleteMapping("/modules/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable String moduleId) {
        // Check if the module exists
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (!optionalModule.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if module not found
        }

        // Delete the module if it exists
        moduleRepository.deleteById(moduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return 204 on successful deletion
    }

    @DeleteMapping("/modules/{moduleId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable String moduleId, @PathVariable String questionId) {

        // Find the module by ID
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (!optionalModule.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if module not found
        }

        Module module = optionalModule.get();

        // Find and remove the question by ID from the module's question list
        boolean questionRemoved = module.getQuestions().removeIf(q -> q.getId().equals(questionId));

        if (!questionRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if question not found
        }

        // Save the updated module without the question
        moduleRepository.save(module);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return 204 No Content on success
    }

}
