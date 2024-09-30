package com.mohanraj.assessment;  // Package declaration for the main application

import org.springframework.boot.SpringApplication;  // Importing SpringApplication for bootstrapping the application
import org.springframework.boot.autoconfigure.SpringBootApplication;  // Importing SpringBootApplication for auto-configuration

@SpringBootApplication  // Annotation that marks this class as a Spring Boot application
public class AssessmentApplication {

	// Main method which is the entry point of the Spring Boot application
	public static void main(String[] args) {
		// Run the Spring Boot application
		SpringApplication.run(AssessmentApplication.class, args);
	}

}
