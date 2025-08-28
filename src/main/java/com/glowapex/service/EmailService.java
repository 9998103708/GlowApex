package com.glowapex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String FROM_EMAIL = "theopenfoood@gmail.com";
    private static final String CREDENTIALS_SUBJECT = "Your GlowApex Account Details";
    private static final String OTP_SUBJECT = "Your GlowApex OTP Code";

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a custom email with a given subject and body.
     */
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to " + toEmail, e);
        }
    }

    /**
     * Sends credentials to a newly registered user.
     */
    public void sendCredentials(String toEmail, String password) {
        String body = String.format("""
                Welcome to GlowApex!
                
                Your account has been successfully created.
                
                Login Email: %s
                Temporary Password: %s
                
                Please log in and change your password immediately for security.
                
                Regards,
                GlowApex Team
                """, toEmail, password);

        sendEmail(toEmail, CREDENTIALS_SUBJECT, body);
    }

    /**
     * Sends a login OTP to the user.
     */
    public void sendOtpEmail(String toEmail, String otp) {
        String body = String.format("""
                Your GlowApex Login OTP is: %s
                
                This OTP is valid for 5 minutes.
                
                If you did not request this, please ignore this email.
                
                Regards,
                GlowApex Team
                """, otp);

        sendEmail(toEmail, OTP_SUBJECT, body);
    }
}
