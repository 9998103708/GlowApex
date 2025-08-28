package com.glowapex.service;

import com.glowapex.entity.OtpEntity;
import com.glowapex.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    // Generate and send OTP
    public void generateAndSendOtp(String email) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Set expiry 5 minutes
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        // Save OTP in DB (overwrite if exists)
        otpRepository.findByEmail(email)
                .ifPresent(existing -> otpRepository.delete(existing));

        otpRepository.save(new OtpEntity(email, otp, expiry));

        // Send email via SendGrid
        String subject = "Your GlowApex OTP Code";
        String body = "Dear user,\n\nYour OTP code is: " + otp +
                "\nIt is valid for 5 minutes.\n\n" +
                "Regards,\nGlowApex Team";

        emailService.sendEmail(email, subject, body);
    }

    // Verify OTP
    public boolean verifyOtp(String email, String otp) {
        return otpRepository.findByEmail(email)
                .map(otpEntity -> {
                    if (otpEntity.getExpiry().isBefore(LocalDateTime.now())) {
                        otpRepository.delete(otpEntity); // Remove expired OTP
                        return false;
                    }
                    boolean valid = otpEntity.getOtp().equals(otp);
                    if (valid) otpRepository.delete(otpEntity); // Remove used OTP
                    return valid;
                })
                .orElse(false);
    }
}
