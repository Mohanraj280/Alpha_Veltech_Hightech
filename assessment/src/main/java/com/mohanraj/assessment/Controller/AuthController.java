package com.mohanraj.assessment.Controller;

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

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/authenticate")
    public ModelAndView authenticate(@RequestParam("username") String username,@RequestParam("email") String email,
                                     @RequestParam("password") String password, HttpSession session) {

        session.setAttribute("username",username);
        session.setAttribute("email",email);

        User user= userRepo.findByUsername(username);
        if ("123".equals(password) && user.getUsername().equals(username) && user.getEmail().equals(email)) {
            return new ModelAndView("redirect:/systemCheck.html");
        } else {
            // If password is incorrect, redirect back to main.html with an error
            ModelAndView modelAndView = new ModelAndView("redirect:/main.html");
            modelAndView.addObject("error", "Invalid username or password");
            return modelAndView;
        }
    }
}
