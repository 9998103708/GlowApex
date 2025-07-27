package com.glowapex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String toEmail, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("glowapex81@gmail.com"); // Set your sender email here
            message.setTo(toEmail);
            message.setSubject("Your account has been created");
            message.setText("Welcome!\nYour account has been created.\nUsername: " + toEmail + "\nPassword: " + password);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}