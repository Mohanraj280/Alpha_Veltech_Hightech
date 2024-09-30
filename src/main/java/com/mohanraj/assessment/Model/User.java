package com.mohanraj.assessment.Model;  // Package declaration for the Model layer

import org.springframework.data.annotation.Id;  // Importing Id annotation for MongoDB document ID
import org.springframework.data.mongodb.core.mapping.Document;  // Importing Document annotation for MongoDB mapping

@Document(collection = "users")  // Annotation to indicate this class is a MongoDB document and maps to the "users" collection
public class User {  // Class definition for User

    @Id  // Annotation to indicate this field is the primary key for the document
    private String id;  // Unique identifier for the user

    private String username;  // Username of the user
    private String email;  // Email address of the user

    // Getter for the user ID
    public String getId() {
        return id;  // Return the ID of the user
    }

    // Setter for the user ID
    public void setId(String id) {
        this.id = id;  // Set the ID of the user
    }

    // Getter for the username
    public String getUsername() {
        return username;  // Return the username of the user
    }

    // Setter for the username
    public void setUsername(String username) {
        this.username = username;  // Set the username of the user
    }

    // Getter for the email
    public String getEmail() {
        return email;  // Return the email of the user
    }

    // Setter for the email
    public void setEmail(String email) {
        this.email = email;  // Set the email of the user
    }
}
