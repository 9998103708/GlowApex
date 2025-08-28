package com.glowapex.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final String CREDENTIALS_SUBJECT = "Your GlowApex Account Details";
    private static final String OTP_SUBJECT = "Your GlowApex OTP Code";
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    @Value("${sendgrid.from.email}")
    private String fromEmail;

    /**
     * Sends a custom email with a given subject and body using SendGrid API.
     */
    public void sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Email sent to: " + toEmail +
                    " | Status: " + response.getStatusCode());
        } catch (IOException e) {
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