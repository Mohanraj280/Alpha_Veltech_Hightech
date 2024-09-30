package com.mohanraj.assessment.Controller;  // Package declaration for the AssessmentController

import com.mohanraj.assessment.Model.Module;  // Importing the Module model class
import com.mohanraj.assessment.Model.Question;  // Importing the Question model class
import com.mohanraj.assessment.Model.StudentResults;
import com.mohanraj.assessment.Service.ModuleService;  // Importing the ModuleService class
import com.mohanraj.assessment.Service.QuestionService;  // Importing the QuestionService class
import com.mohanraj.assessment.Service.StudentResultsService;
import org.springframework.beans.factory.annotation.Autowired;  // Importing Autowired annotation for dependency injection
import org.springframework.http.HttpEntity;  // Importing HttpEntity class for HTTP requests
import org.springframework.http.MediaType;  // Importing MediaType for setting content types
import org.springframework.http.ResponseEntity;  // Importing ResponseEntity class for HTTP responses
import org.springframework.mail.SimpleMailMessage;  // Importing SimpleMailMessage for sending emails
import org.springframework.mail.javamail.JavaMailSender;  // Importing JavaMailSender for email sending functionality
import org.springframework.web.bind.annotation.*;  // Importing Spring MVC annotations for RESTful APIs

import jakarta.servlet.http.HttpSession;  // Importing HttpSession for managing user sessions
import org.springframework.web.client.RestTemplate;  // Importing RestTemplate for making HTTP requests

import org.springframework.http.HttpHeaders;  // Importing HttpHeaders for setting request headers

import java.time.LocalDateTime;
import java.util.ArrayList;  // Importing ArrayList for dynamic array functionality
import java.util.List;  // Importing List interface for collections
import java.util.Map;  // Importing Map interface for key-value pairs
import java.util.Optional;  // Importing Optional for handling null values safely

@RestController  // Annotation to indicate that this class is a RESTful controller
@RequestMapping("/api")  // Base URL mapping for this controller
public class AssessmentController {  // Class definition for AssessmentController

    @Autowired  // Annotation for dependency injection of QuestionService
    private QuestionService questionService;  // Instance of QuestionService for question operations

    @Autowired
    private StudentResultsService studentResultsService;

    @Autowired  // Annotation for dependency injection of ModuleService
    private ModuleService moduleService;  // Instance of ModuleService for module operations

    @Autowired  // Annotation for dependency injection of JavaMailSender
    private JavaMailSender mailSender;  // Instance of JavaMailSender for sending emails

    @PostMapping("/submit-answers")  // Endpoint to handle POST requests for submitting answers
    public Map<String, Object> submitAnswers(@RequestBody Map<String, Object> userAnswers, HttpSession session) {
        // Extract username and email from the session
        String username = (String) session.getAttribute("username");
        String userEmail = (String) session.getAttribute("email");
        String moduleId = (String) userAnswers.get("moduleId");  // Retrieve moduleId from the request body

        // Validate that 'answers' exists and is a Map
        if (userAnswers == null || !userAnswers.containsKey("answers") || !(userAnswers.get("answers") instanceof Map)) {
            // Return an error response if answers data is invalid
            return Map.of("success", false, "message", "Invalid answers data");
        }

        System.out.println(moduleId);  // Log the moduleId for debugging
        if (moduleId == null) {  // Check if moduleId is null
            throw new IllegalArgumentException("Module ID must not be null");  // Throw an exception if null
        }

        @SuppressWarnings("unchecked")  // Suppress unchecked cast warning
        Map<String, Map<String, Object>> answers = (Map<String, Map<String, Object>>) userAnswers.get("answers");  // Retrieve answers
        System.out.println(answers);  // Log the user's answers for debugging

        // Get questions from the specified module
        Optional<Module> module = moduleService.getModuleById(moduleId);  // Retrieve module by ID
        List<Question> questions = module.get().getQuestions();  // Get questions from the module

        // List to hold wrong answered questions
        List<Question> wrongAnsweredQuestions = new ArrayList<>();  // Initialize a list for wrong answers

        int score = 0;  // Initialize score counter
        int totalQuestions = questions.size();  // Get the total number of questions

        // Loop through each question to evaluate answers
        for (Question question : questions) {
            String questionId = question.getId();  // Get the question ID
            String correctAnswer = question.getCorrectAnswer();  // Get the correct answer

            // Get the user's answer object for the current question ID
            Map<String, Object> userAnswerObj = answers.get(questionId); // Get the user's answer as a Map
            String userAnswer = (userAnswerObj != null && userAnswerObj.containsKey("answer"))
                    ? (String) userAnswerObj.get("answer")
                    : null; // Extract the answer string

            System.out.println(userAnswer + " User ");
            System.out.println(correctAnswer + " Correct");

            // Check if the user's answer matches the correct answer
            if (userAnswer != null && correctAnswer.equalsIgnoreCase(userAnswer.trim())) {
                score++;  // Increment score for correct answer
            } else {
                // Add wrong answered question to the list
                wrongAnsweredQuestions.add(question);  // Add question to wrong answers list
            }
        }
        List<String> correctAnswers = new ArrayList<>();  // List to hold feedback for wrong answers

        System.out.println("Wrong Answered Questions:");  // Log header for wrong questions
        for (Question wrongQuestion : wrongAnsweredQuestions) {  // Loop through wrong answered questions
            correctAnswers.add("Candidate answer is incorrect for this question ");  // Add feedback
            correctAnswers.add(wrongQuestion.getQuestionText());  // Add the question text
            correctAnswers.add("but the correct answer is");  // Add feedback statement
            correctAnswers.add(wrongQuestion.getCorrectAnswer());  // Add the correct answer
            // Uncomment to log detailed information about wrong questions
            // System.out.println("Question ID: " + wrongQuestion.getId() + ", Question Text: " + wrongQuestion.getQuestionText() + ", Correct Answer: " + wrongQuestion.getCorrectAnswer());
        }

        System.out.println(correctAnswers);  // Log the feedback for wrong answers
        // Generate skill improvement recommendations based on the results
        String recommendations = getChatbotRecommendation(username, score, String.valueOf(correctAnswers), totalQuestions, generateRecommendations(score, totalQuestions));

        // Send email with error handling
        try {
            sendResultsEmail(userEmail, username, score, totalQuestions, recommendations);  // Attempt to send email with results
        } catch (Exception e) {  // Catch any exceptions during email sending
            // Return an error response if sending fails
            return Map.of("success", false, "message", "Error sending results email: " + e.getMessage());
        }
        if (session != null) {
            session.invalidate();  // Invalidate the session to delete all session data
        }  // Invalidate the session to delete all session data
        // Return a success response if everything is processed correctly
        return Map.of("success", true, "message", "Results submitted successfully.");
    }

    private String generateRecommendations(int score, int totalQuestions) {
        // Placeholder logic for generating recommendations based on score
        if (score < totalQuestions / 2) {  // Check if score is less than half
            return "You need to improve your understanding of basic concepts in your subject. We recommend focusing on foundational topics.";  // Provide improvement feedback
        } else if (score < totalQuestions * 0.8) {  // Check if score is less than 80%
            return "You have a good grasp of basic concepts. To further improve, focus on advanced topics and practice more complex problems.";  // Provide advancement feedback
        } else {
            return "Excellent performance! Continue honing your skills with advanced topics and try challenging problems to maintain your proficiency.";  // Congratulatory feedback
        }
    }

    private void sendResultsEmail(String to, String username, int score, int totalQuestions, String recommendations) {
        SimpleMailMessage message = new SimpleMailMessage();  // Create a new email message
        message.setTo(to);  // Set the recipient's email address
        message.setSubject("Assessment Results");  // Set the subject of the email
        message.setText("Dear " + username + ",\n\n" +  // Set the body of the email
                "Thank you for completing the assessment.\n\n" +
                "Your score: " + score + "/" + totalQuestions + "\n\n" +
                "Recommendations for improvement:\n" + recommendations + "\n\n" +
                "Best regards,\nAssessment Team");
        StudentResults results=new StudentResults(username,to,score,totalQuestions,recommendations, LocalDateTime.now());
        studentResultsService.saveStudentResults(results);
        mailSender.send(message);  // Send the email message
    }

    private String getChatbotRecommendation(String username, int score, String wrong, int totalQuestions, String recommands) {
        RestTemplate restTemplate = new RestTemplate();  // Create a RestTemplate for making HTTP requests
        String chatbotUrl = "http://localhost:3000/.netlify/functions/api/chat";  // Define the URL for the chatbot server

        // Format the user input for the chatbot with user information and results
        String userInput = String.format("User %s scored %d out of %d. %s ,%s Provide recommendations for improvement based on their answers.", username, score, totalQuestions, recommands, wrong);

        HttpHeaders headers = new HttpHeaders();  // Create headers for the HTTP request
        headers.setContentType(MediaType.APPLICATION_JSON);  // Set the content type to JSON

        // Create a JSON request body with the user input
        String requestJson = String.format("{\"userInput\": \"%s\"}", userInput);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);  // Create an HTTP entity with the request body and headers

        ResponseEntity<String> response = restTemplate.postForEntity(chatbotUrl, entity, String.class);  // Send POST request to the chatbot server
        System.out.println(response.getBody());  // Log the response from the chatbot for debugging
        return formatRecommendations(response.getBody());  // Format and return the chatbot's recommendations
    }

    private String formatRecommendations(String response) {
        // Remove unwanted characters and format the response
        if (response == null || response.isEmpty()) {  // Check if the response is null or empty
            return "";  // Return an empty string if there is no response
        }

        // Clean the response by removing unwanted characters and formatting
        String formattedText = response
                .replaceAll("\\{", "")  // Remove curly braces
                .replaceAll("\\}", "")  // Remove curly braces
                .replaceAll("\\\\n", "\n")  // Replace \n with newlines
                .replaceAll("\\*\\*", "")  // Remove ** (bold formatting)
                .replaceAll("([0-9]+)\\.", "$1.")  // Ensure numbering is kept intact
                .replaceAll("^[\\d\\.]+", "")  // Remove leading numbers (if any)
                .trim();  // Remove leading and trailing whitespace

        return formattedText;  // Return the cleaned and formatted text
    }
}
