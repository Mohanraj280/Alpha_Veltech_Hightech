package com.mohanraj.assessment.Controller;

import com.mohanraj.assessment.Model.User;
import com.mohanraj.assessment.Model.UserLoginRequest;
import com.mohanraj.assessment.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ModelAndView authenticate(@RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     HttpSession session) {

        session.setAttribute("username", username);
        session.setAttribute("email", email);

        try {
            if (userService.authenticateUser(username, email, password)) {
                return new ModelAndView("redirect:/CandidatePages/CandidateDashboard.html");
            } else {
                ModelAndView modelAndView = new ModelAndView("redirect:/CandidatePages/main.html");
                modelAndView.addObject("error", "Invalid username or password");
                return modelAndView;
            }
        } catch (Exception e) {
            // Log the error (optional)
            ModelAndView modelAndView = new ModelAndView("redirect:/errorPage.html");
            modelAndView.addObject("error", "Something went wrong. Please try again later.");
            return modelAndView;
        }
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password, HttpSession session) {

        session.setAttribute("username", username);

        try {
            if (userService.authenticateAdmin(username, password)) {
                return new ModelAndView("redirect:/AdminPages/dashboard.html");
            } else {
                ModelAndView modelAndView = new ModelAndView("redirect:/AdminPages/admin.html");
                modelAndView.addObject("error", "Invalid username or password");
                return modelAndView;
            }
        } catch (Exception e) {
            // Log the error (optional)
            ModelAndView modelAndView = new ModelAndView("redirect:/errorPage.html");
            modelAndView.addObject("error", "Something went wrong. Please try again later.");
            return modelAndView;
        }
    }
}
