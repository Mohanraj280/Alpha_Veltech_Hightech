package com.mohanraj.assessment.Controller;

import com.mohanraj.assessment.Model.Question;
import com.mohanraj.assessment.Repository.QuestionRepository;
import com.mohanraj.assessment.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable String id) {
        return questionService.getQuestionById(id);
    }

    // Create a new question
    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question savedQuestion = questionRepository.save(question);
        return ResponseEntity.ok(savedQuestion);
    }

    // Update a question
    @PutMapping("/update/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable String id, @RequestBody Question updatedQuestion) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (((Optional<?>) existingQuestion).isPresent()) {
            Question question = existingQuestion.get();
            question.setQuestionText(updatedQuestion.getQuestionText());
            question.setOptions(updatedQuestion.getOptions());
            question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            Question savedQuestion = questionRepository.save(question);
            return ResponseEntity.ok(savedQuestion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a question
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (existingQuestion.isPresent()) {
            questionRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
