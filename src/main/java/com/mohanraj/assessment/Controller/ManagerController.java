package com.mohanraj.assessment.Controller;

import com.mohanraj.assessment.Model.Module;
import com.mohanraj.assessment.Model.StudentResults;
import com.mohanraj.assessment.Model.User;
import com.mohanraj.assessment.Model.UserLoginRequest;
import com.mohanraj.assessment.Repository.ModuleRepository;
import com.mohanraj.assessment.Repository.StudentResultsRepository;
import com.mohanraj.assessment.Repository.UserLoginRequestRepo;
import com.mohanraj.assessment.Repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ManagerController {

    @Autowired
    private UserLoginRequestRepo userRepo;
    @Autowired
    private StudentResultsRepository studentResultsRepository;

    @Autowired
    private UserRepo user;

    @Autowired  // Annotation for dependency injection of ModuleRepository
    private ModuleRepository moduleRepository;  // Instance of ModuleRepository for database operations

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestParam String username, @RequestParam String password) {
        Map<String, String> response = new HashMap<>();

        if (userRepo.findByUsername(username) != null) {

            response.put("status", "error");
            response.put("message", "Username already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        UserLoginRequest users=new UserLoginRequest();
        users.setUsername(username);
        users.setPassword(password);
        userRepo.save(users);


        response.put("status", "success");
        response.put("message", "Registration successful!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Studentregister")
    public ResponseEntity<Map<String, String>> registerStudent(@RequestParam String username, @RequestParam String email) {
        Map<String, String> response = new HashMap<>();

        if (user.findByUsername(username) != null || user.findByEmail(email) != null) {
            response.put("status", "error");
            response.put("message", "Username already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        User users = new User();
        users.setUsername(username);
        users.setEmail(email);
        user.save(users);

        response.put("status", "success");
        response.put("message", "Registration successful!");
        return ResponseEntity.ok(response);


    }

    @GetMapping("/api/modules")  // Endpoint to handle GET requests for retrieving modules
    public ResponseEntity<?> showModules(HttpSession session) {  // Accepting HttpSession to check user authentication

        // Return the list of modules as JSON
        List<Module> modules = moduleRepository.findAll();  // Retrieve all modules from the database
        return ResponseEntity.ok(modules);  // Return modules with 200 OK status
    }

    @GetMapping("/student-results")
    public ResponseEntity<List<StudentResults>> getAllStudentResults() {
        List<StudentResults> results = studentResultsRepository.findAll();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
