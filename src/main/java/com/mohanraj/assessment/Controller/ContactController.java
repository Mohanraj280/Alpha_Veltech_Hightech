package com.mohanraj.assessment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/contactmail")
    public String contactMail(@RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String subject,
                              @RequestParam String messageContent,
                              Model model) {
        // Create a new email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);  // Set the recipient's email address
        message.setSubject(subject);  // Set the subject of the email
        message.setText("Dear " + username + ",\n\n" +
                "Thank you for your query.\n\n" +
                "Message: " + messageContent + "\n\n" +
                "Best regards,\nAlpha Team");

        // Send the email message
        mailSender.send(message);

        // Add a success message to the model
        model.addAttribute("successMessage", "Your query has been submitted successfully!");

        // Redirect to a confirmation page (you can also render the same page with a message)
        return "redirect:/Index/thank-you.html"; // Assuming you have a thank you page
    }
}
