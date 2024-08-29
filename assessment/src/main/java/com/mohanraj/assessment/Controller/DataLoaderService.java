package com.mohanraj.assessment.Controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DataLoaderService {

    @Autowired
    private QuestionRepository questionRepository;

    @PostConstruct
    public void loadSampleData() {
        Question question1 = new Question();
//        question1.setQuestionText("What does the acronym \"OOP\" stand for in programming?");
//        question1.setOptions(Arrays.asList("Object-Oriented Programming", "Out-of-Order Processing", "Original Operating Procedure", "Overloaded Object Protocol"));
//        question1.setCorrectAnswer("Object-Oriented Programming");
//
//        Question question2 = new Question();
//        question2.setQuestionText("Which of the following is not a programming language?");
//        question2.setOptions(Arrays.asList("Python", "HTML", "Java", "C#"));
//        question2.setCorrectAnswer("HTML");
//
//        Question question3 = new Question();
//        question3.setQuestionText("In Java, which keyword is used to define a class?");
//        question3.setOptions(Arrays.asList("class", "define", "struct", "new"));
//        question3.setCorrectAnswer("class");
//
//        Question question4 = new Question();
//        question4.setQuestionText("What does SQL stand for?");
//        question4.setOptions(Arrays.asList("Structured Query Language", "Simple Query Language", "Standard Query Language", "Server Query Language"));
//        question4.setCorrectAnswer("Structured Query Language");
//
//        Question question5 = new Question();
//        question5.setQuestionText("Which of the following is a loop structure in C++?");
//        question5.setOptions(Arrays.asList("for", "while", "do-while", "All of the above"));
//        question5.setCorrectAnswer("All of the above");
//
//        Question question6 = new Question();
//        question6.setQuestionText("Which of the following is used to create an object in Java?");
//        question6.setOptions(Arrays.asList("new", "create", "object", "instantiate"));
//        question6.setCorrectAnswer("new");
//
//        Question question7 = new Question();
//        question7.setQuestionText("Which of the following is a Python framework for web development?");
//        question7.setOptions(Arrays.asList("Django", "Laravel", "Spring", "Angular"));
//        question7.setCorrectAnswer("Django");
//
//        Question question8 = new Question();
//        question8.setQuestionText("Which data structure uses LIFO (Last In, First Out) principle?");
//        question8.setOptions(Arrays.asList("Stack", "Queue", "Array", "Tree"));
//        question8.setCorrectAnswer("Stack");
//
//        Question question9 = new Question();
//        question9.setQuestionText("What is the main difference between Java and JavaScript?");
//        question9.setOptions(Arrays.asList("Java is a compiled language; JavaScript is an interpreted language", "Java is used for backend; JavaScript is used for frontend", "Java is a scripting language; JavaScript is a programming language", "Both are the same"));
//        question9.setCorrectAnswer("Java is a compiled language; JavaScript is an interpreted language");
//
//        Question question10 = new Question();
//        question10.setQuestionText("Which of the following is used to declare a constant in C++?");
//        question10.setOptions(Arrays.asList("const", "final", "define", "constant"));
//        question10.setCorrectAnswer("const");
//
//        // Save to MongoDB
//        questionRepository.saveAll(Arrays.asList(
//                question1, question2, question3, question4, question5,
//                question6, question7, question8, question9, question10
//        ));

        System.out.println("Sample programming questions loaded into MongoDB.");
    }
}
