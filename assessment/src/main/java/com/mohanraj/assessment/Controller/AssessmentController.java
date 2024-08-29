package com.mohanraj.assessment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AssessmentController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/submit-answers")
    public Map<String, Object> submitAnswers(@RequestBody Map<String, Object> userAnswers, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String userEmail = (String) session.getAttribute("email");

        // Validate that 'answers' exists and is a Map
        if (userAnswers == null || !userAnswers.containsKey("answers") || !(userAnswers.get("answers") instanceof Map)) {
            return Map.of("success", false, "message", "Invalid answers data");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> answers = (Map<String, String>) userAnswers.get("answers");

        // Get correct answers from database
        List<Question> questions = questionService.getAllQuestions();
        int score = 0;
        int totalQuestions = questions.size();

        for (Question question : questions) {
            String questionId = question.getId();
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = answers.get(questionId);

            if (userAnswer != null && correctAnswer.equals(userAnswer.trim())) {
                score++;
            }
        }

        // Generate skill improvement recommendations
        String recommendations = getChatbotRecommendation(username,score, totalQuestions,generateRecommendations(score,totalQuestions));

        // Send email with error handling
        try {
            sendResultsEmail(userEmail, username, score, totalQuestions, recommendations);
        } catch (Exception e) {
            return Map.of("success", false, "message", "Error sending results email: " + e.getMessage());
        }

        return Map.of("success", true, "message", "Results submitted successfully.");
    }

    private String generateRecommendations(int score, int totalQuestions) {
        // Placeholder logic for generating recommendations based on score
        if (score < totalQuestions / 2) {
            return "You need to improve your understanding of basic concepts in your subject. We recommend focusing on foundational topics.";
        } else if (score < totalQuestions * 0.8) {
            return "You have a good grasp of basic concepts. To further improve, focus on advanced topics and practice more complex problems.";
        } else {
            return "Excellent performance! Continue honing your skills with advanced topics and try challenging problems to maintain your proficiency.";
        }
    }

    private void sendResultsEmail(String to, String username, int score, int totalQuestions, String recommendations) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Assessment Results");
        message.setText("Dear " + username + ",\n\n" +
                "Thank you for completing the assessment.\n\n" +
                "Your score: " + score + "/" + totalQuestions + "\n\n" +
                "Recommendations for improvement:\n" + recommendations + "\n\n" +
                "Best regards,\nAssessment Team");

        mailSender.send(message);
    }


    private String getChatbotRecommendation(String username, int score, int totalQuestions, String recommands) {
        RestTemplate restTemplate = new RestTemplate();
        String chatbotUrl = "http://localhost:3000/chat";  // Change to your chatbot server URL

        String userInput = String.format("User %s scored %d out of %d. %s , Provide recommendations for improvement.", username, score, totalQuestions,recommands);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format("{\"userInput\": \"%s\"}", userInput);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(chatbotUrl, entity, String.class);
        System.out.println(response.getBody());
        return formatRecommendations(response.getBody());
    }

    private String formatRecommendations(String response) {
        // Remove unwanted characters and format the response
        if (response == null || response.isEmpty()) {
            return "";
        }

        // Clean the response
        String formattedText = response
                .replaceAll("\\{", "")  // Remove curly braces
                .replaceAll("\\}", "")  // Remove curly braces
                .replaceAll("\\\\n", "\n")  // Replace \n with newlines
                .replaceAll("\\*\\*", "")  // Remove ** (bold)
                .replaceAll("([0-9]+)\\.", "$1.")  // Ensure numbering is kept intact
                .replaceAll("^[\\d\\.]+", "")  // Remove leading numbers (if any)
                .trim();  // Remove leading and trailing whitespace

        return formattedText;
    }
}
