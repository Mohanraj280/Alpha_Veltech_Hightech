package com.mohanraj.assessment.Model;  // Package declaration for the Model layer

import org.springframework.data.annotation.Id;  // Importing Id annotation for MongoDB document ID
import org.springframework.data.mongodb.core.mapping.Document;  // Importing Document annotation for MongoDB mapping
import java.util.List;  // Importing List interface for handling lists

@Document(collection = "exam_questions")  // Annotation to indicate this class is a MongoDB document and maps to the "exam_questions" collection
public class Question {  // Class definition for Question

    @Id  // Annotation to indicate this field is the primary key for the document
    private String id;  // Unique identifier for the question

    private String questionText;  // Text of the question
    private List<String> options;  // List of answer options for the question
    private String correctAnswer;  // The correct answer for the question

    // Getters and Setters for encapsulated fields

    public String getId() {  // Getter for the question ID
        return id;  // Return the ID of the question
    }

    public String getQuestionText() {  // Getter for the question text
        return questionText;  // Return the text of the question
    }

    public void setQuestionText(String questionText) {  // Setter for the question text
        this.questionText = questionText;  // Set the text of the question
    }

    public List<String> getOptions() {  // Getter for the list of options
        return options;  // Return the list of answer options
    }

    public void setOptions(List<String> options) {  // Setter for the list of options
        this.options = options;  // Set the list of answer options
    }

    public String getCorrectAnswer() {  // Getter for the correct answer
        return correctAnswer;  // Return the correct answer for the question
    }

    public void setCorrectAnswer(String correctAnswer) {  // Setter for the correct answer
        this.correctAnswer = correctAnswer;  // Set the correct answer for the question
    }
}
