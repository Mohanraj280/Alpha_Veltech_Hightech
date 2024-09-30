package com.mohanraj.assessment.Service;  // Package declaration for the Service layer

import com.mohanraj.assessment.Model.Question;  // Importing the Question model
import com.mohanraj.assessment.Repository.QuestionRepository;  // Importing the QuestionRepository interface
import org.springframework.beans.factory.annotation.Autowired;  // Importing the Autowired annotation
import org.springframework.stereotype.Service;  // Importing the Service annotation
import java.util.List;  // Importing List for returning multiple questions

@Service  // Annotation to indicate that this class is a service component
public class QuestionService {

    @Autowired  // Automatically injects the QuestionRepository bean
    private QuestionRepository questionRepository;

    // Get all questions from the database
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();  // Returns a list of all questions from the repository
    }

    // Get a question by its ID
    public Question getQuestionById(String id) {
        return questionRepository.findById(id).orElse(null);  // Returns the question if found, otherwise null
    }
}
