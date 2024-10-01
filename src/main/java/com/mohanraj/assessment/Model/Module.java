package com.mohanraj.assessment.Model;  // Package declaration for the Model layer

import org.springframework.data.annotation.Id;  // Importing Id annotation for MongoDB document ID
import org.springframework.data.mongodb.core.mapping.Document;  // Importing Document annotation for MongoDB mapping
import java.util.List;  // Importing List interface for handling lists

@Document(collection = "modules")  // Annotation to indicate this class is a MongoDB document and maps to the "modules" collection
public class Module {  // Class definition for Module

    @Id  // Annotation to indicate this field is the primary key for the document
    private String id;  // Unique identifier for the module

    private String moduleName;  // Name of the module
    private String moduleDescription;  // Description of the module
    private List<Question> questions;  // List of questions associated with the module

    // Getters and Setters for encapsulated fields

    public String getId() {  // Getter for the module ID
        return id;  // Return the ID of the module
    }

    public void setId(String id) {  // Setter for the module ID
        this.id = id;  // Set the ID of the module
    }

    public String getModuleName() {  // Getter for the module name
        return moduleName;  // Return the name of the module
    }

    public void setModuleName(String moduleName) {  // Setter for the module name
        this.moduleName = moduleName;  // Set the name of the module
    }

    public String getModuleDescription() {  // Getter for the module description
        return moduleDescription;  // Return the description of the module
    }

    public void setModuleDescription(String moduleDescription) {  // Setter for the module description
        this.moduleDescription = moduleDescription;  // Set the description of the module
    }

    public List<Question> getQuestions() {  // Getter for the list of questions
        return questions;  // Return the list of questions associated with the module
    }

    public void setQuestions(List<Question> questions) {  // Setter for the list of questions
        this.questions = questions;  // Set the list of questions for the module
    }
}
