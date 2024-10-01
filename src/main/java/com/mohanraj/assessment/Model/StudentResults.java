package com.mohanraj.assessment.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "studentResults")  // MongoDB collection name
public class StudentResults {

    @Id
    private String id;  // Unique identifier for the result record

    private String username;        // Username of the student
    private String email;           // Email of the student
    private int score;              // Score obtained by the student
    private int totalQuestions;      // Total questions in the exam
    private String recommendations;

    private LocalDateTime date;  // Date when the exam was taken
// Recommendations based on the student's performance

    // Constructor
    public StudentResults(String username, String email, int score, int totalQuestions, String recommendations, LocalDateTime date) {
        this.username = username;
        this.email = email;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.recommendations = recommendations;
        this.date = date;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
