package com.mohanraj.assessment.Controller;  // Package declaration for the PageController

import jakarta.servlet.http.HttpSession;  // Importing HttpSession for managing user sessions
import org.springframework.http.HttpStatus;  // Importing HttpStatus for HTTP response status codes
import org.springframework.http.ResponseEntity;  // Importing ResponseEntity for building HTTP responses
import org.springframework.stereotype.Controller;  // Importing Controller annotation for MVC controllers
import org.springframework.web.bind.annotation.GetMapping;  // Importing GetMapping annotation for GET requests

@Controller  // Annotation to indicate that this class is a Spring MVC controller
public class PageController {  // Class definition for PageController

    @GetMapping("/")  // Mapping root URL ("/") to this method for handling GET requests
    public String main() {  // Method to handle main page request
        return "Index/index.html";  // Return the view name for the main page
    }

    @GetMapping("/ManagerDashboard")  // Mapping root URL ("/ManagerDashboard") to this method for handling GET requests
    public String manager() {  // Method to handle manager page request
        return "ManagerDashboard/ManagerDashboard.html";  // Return the view name for the ManagerDashboard page
    }


    @GetMapping("/dashboardAuth")  // Mapping "/dashboardAuth" URL to this method for handling GET requests
    public ResponseEntity<?> showModules(HttpSession session) {  // Accepting HttpSession to check user authentication
        String username = (String) session.getAttribute("username");  // Retrieve username from the session

        System.out.println(username);  // Log the username to the console
        if (username == null) {  // Check if the session attribute is missing
            // If session attributes are missing, return a 403 Forbidden response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");  // Return forbidden access message
        }
        // If the user is authenticated, return the modules or any required data
        return ResponseEntity.ok("Authorized"); // Return a success message indicating the user is authorized
    }
}
