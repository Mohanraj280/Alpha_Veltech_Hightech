package com.mohanraj.assessment.Controller;  // Package declaration for the QuestionController

import com.mohanraj.assessment.Model.Question;  // Importing the Question model class
import com.mohanraj.assessment.Repository.QuestionRepository;  // Importing the QuestionRepository for database operations
import com.mohanraj.assessment.Service.QuestionService;  // Importing the QuestionService for business logic
import org.springframework.beans.factory.annotation.Autowired;  // Importing Autowired for dependency injection
import org.springframework.http.ResponseEntity;  // Importing ResponseEntity for building HTTP responses
import org.springframework.web.bind.annotation.*;  // Importing annotations for RESTful web services
import java.util.List;  // Importing List interface for handling lists
import java.util.Optional;  // Importing Optional for handling optional values

@RestController  // Annotation to indicate that this class is a Spring REST controller
@RequestMapping("/api/questions")  // Base URL mapping for the controller
public class QuestionController {  // Class definition for QuestionController

    @Autowired  // Autowiring the QuestionService bean
    private QuestionService questionService;

    @Autowired  // Autowiring the QuestionRepository bean
    private QuestionRepository questionRepository;

    @GetMapping  // Mapping GET requests for the base URL ("/api/questions")
    public List<Question> getAllQuestions() {  // Method to retrieve all questions
        return questionService.getAllQuestions();  // Return the list of all questions from the service
    }

    @GetMapping("/{id}")  // Mapping GET requests for a specific question by ID
    public Question getQuestionById(@PathVariable String id) {  // Method to retrieve a question by its ID
        return questionService.getQuestionById(id);  // Return the question with the specified ID
    }

    // Create a new question
    @PostMapping("/create")  // Mapping POST requests to create a new question
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {  // Method to create a new question
        Question savedQuestion = questionRepository.save(question);  // Save the new question to the repository
        return ResponseEntity.ok(savedQuestion);  // Return the saved question in the response
    }

    // Update a question
    @PutMapping("/update/{id}")  // Mapping PUT requests to update an existing question by ID
    public ResponseEntity<Question> updateQuestion(@PathVariable String id, @RequestBody Question updatedQuestion) {  // Method to update a question
        Optional<Question> existingQuestion = questionRepository.findById(id);  // Find the existing question by ID
        if (existingQuestion.isPresent()) {  // Check if the question exists
            Question question = existingQuestion.get();  // Retrieve the existing question
            question.setQuestionText(updatedQuestion.getQuestionText());  // Update the question text
            question.setOptions(updatedQuestion.getOptions());  // Update the question options
            question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());  // Update the correct answer
            Question savedQuestion = questionRepository.save(question);  // Save the updated question
            return ResponseEntity.ok(savedQuestion);  // Return the updated question in the response
        } else {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if the question does not exist
        }
    }

    // Delete a question
    @DeleteMapping("/delete/{id}")  // Mapping DELETE requests to delete a question by ID
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {  // Method to delete a question
        Optional<Question> existingQuestion = questionRepository.findById(id);  // Find the existing question by ID
        if (existingQuestion.isPresent()) {  // Check if the question exists
            questionRepository.deleteById(id);  // Delete the question from the repository
            return ResponseEntity.ok().build();  // Return 200 OK response after deletion
        } else {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if the question does not exist
        }
    }
}
