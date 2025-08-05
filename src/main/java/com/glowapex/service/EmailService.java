package com.glowapex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String FROM_EMAIL = "glowapex81@gmail.com";
    private static final String CREDENTIALS_SUBJECT = "Your account has been created";
    private static final String OTP_SUBJECT = "Your Login OTP Code";

    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String toEmail, String password) {
        String body = "Welcome!\n\nYour account has been created.\n\nUsername: " + toEmail +
                "\nPassword: " + password + "\n\nPlease log in and change your password.";
        sendSimpleEmail(toEmail, CREDENTIALS_SUBJECT, body);
        System.out.println("Credentials email sent to: " + toEmail);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        String body = "Your OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\n" +
                "If you didn’t request this, please ignore this email.";
        sendSimpleEmail(toEmail, OTP_SUBJECT, body);
        System.out.println("OTP email sent to: " + toEmail);
    }

    private void sendSimpleEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}