package com.mohanraj.assessment.Model;  // Package declaration for the Model layer

import org.springframework.data.annotation.Id;  // Importing Id annotation for MongoDB document ID
import org.springframework.data.mongodb.core.mapping.Document;  // Importing Document annotation for MongoDB mapping

@Document(collection = "userLoginRequest")  // Annotation to indicate this class is a MongoDB document and maps to the "userLoginRequest" collection
public class UserLoginRequest {  // Class definition for UserLoginRequest

    @Id  // Annotation to indicate this field is the primary key for the document
    private String id;  // Unique identifier for the login request

    private String username;  // Username provided during login
    private String password;  // Password provided during login

    // Getter for the username
    public String getUsername() {
        return username;  // Return the username from the login request
    }

    // Setter for the username
    public void setUsername(String username) {
        this.username = username;  // Set the username for the login request
    }

    // Getter for the password
    public String getPassword() {
        return password;  // Return the password from the login request
    }

    // Setter for the password
    public void setPassword(String password) {
        this.password = password;  // Set the password for the login request
    }
}
