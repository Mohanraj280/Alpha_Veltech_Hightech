package com.mohanraj.assessment.Controller;  // Package declaration for the AuthController

import com.mohanraj.assessment.Model.User;  // Importing the User model class (currently not used in this class)
import com.mohanraj.assessment.Model.UserLoginRequest;  // Importing the UserLoginRequest model class (currently not used)
import com.mohanraj.assessment.Service.UserService;  // Importing the UserService class for user authentication
import jakarta.servlet.http.HttpSession;  // Importing HttpSession for managing user sessions
import org.springframework.beans.factory.annotation.Autowired;  // Importing Autowired annotation for dependency injection
import org.springframework.stereotype.Controller;  // Importing Controller annotation for Spring MVC controllers
import org.springframework.web.bind.annotation.GetMapping;  // Importing GetMapping for handling GET requests
import org.springframework.web.bind.annotation.PostMapping;  // Importing PostMapping for handling POST requests
import org.springframework.web.bind.annotation.RequestParam;  // Importing RequestParam for extracting query parameters
import org.springframework.web.servlet.ModelAndView;  // Importing ModelAndView for handling responses with views

@Controller  // Annotation to indicate that this class is a Spring MVC controller
public class AuthController {  // Class definition for AuthController

    @Autowired  // Annotation for dependency injection of UserService
    private UserService userService;  // Instance of UserService for user authentication operations

    @PostMapping("/authenticate")  // Endpoint to handle POST requests for user authentication
    public ModelAndView authenticate(@RequestParam("username") String username,  // Extract username from request parameters
                                     @RequestParam("email") String email,  // Extract email from request parameters
                                     @RequestParam("password") String password,  // Extract password from request parameters
                                     HttpSession session) {  // Injected HttpSession to manage user session

        // Store the username and email in the session for later use
        session.setAttribute("username", username);  // Save username in the session
        session.setAttribute("email", email);  // Save email in the session

        try {  // Start of try block for exception handling
            // Authenticate the user using the userService
            if (userService.authenticateUser(username, email, password)) {  // Check if user credentials are valid
                // Redirect to the candidate dashboard if authentication is successful
                return new ModelAndView("redirect:/CandidatePages/CandidateDashboard.html");  // Successful login redirection
            } else {
                // Redirect to the main page with an error message if authentication fails
                ModelAndView modelAndView = new ModelAndView("redirect:/CandidatePages/main.html");  // Create a ModelAndView for redirection
                modelAndView.addObject("error", "Invalid username or password");  // Add error message to the model
                return modelAndView;  // Return the ModelAndView
            }
        } catch (Exception e) {  // Catch any exceptions that occur during authentication
            // Optional: Log the error (not implemented here)
            ModelAndView modelAndView = new ModelAndView("redirect:/errorPage.html");  // Redirect to an error page
            modelAndView.addObject("error", "Something went wrong. Please try again later.");  // Add error message to the model
            return modelAndView;  // Return the ModelAndView
        }
    }

    @PostMapping("/login")  // Endpoint to handle POST requests for admin login
    public ModelAndView login(@RequestParam("username") String username,  // Extract username from request parameters
                              @RequestParam("password") String password,  // Extract password from request parameters
                              HttpSession session) {  // Injected HttpSession to manage user session

        session.setAttribute("username", username);  // Store username in session for admin login

        try {  // Start of try block for exception handling
            // Authenticate the admin using the userService
            if (userService.authenticateAdmin(username, password)) {  // Check if admin credentials are valid
                // Redirect to the admin dashboard if authentication is successful
                return new ModelAndView("redirect:/AdminPages/dashboard.html");  // Successful login redirection
            } else {
                // Redirect to the admin login page with an error message if authentication fails
                ModelAndView modelAndView = new ModelAndView("redirect:/AdminPages/admin.html");  // Create a ModelAndView for redirection
                modelAndView.addObject("error", "Invalid username or password");  // Add error message to the model
                return modelAndView;  // Return the ModelAndView
            }
        } catch (Exception e) {  // Catch any exceptions that occur during authentication
            ModelAndView modelAndView = new ModelAndView("redirect:/errorPage.html");  // Redirect to an error page
            modelAndView.addObject("error", "Something went wrong. Please try again later.");  // Add error message to the model
            return modelAndView;  // Return the ModelAndView
        }
    }
}
