package com.glowapex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage = new HashMap<>();

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        emailService.sendOtpEmail(email, otp);
        System.out.println("OTP generated and sent to: " + email + " | OTP: " + otp); // Debug only
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData data = otpStorage.get(email);
        if (data == null) return false;
        if (data.getExpiry().isBefore(LocalDateTime.now())) return false;
        return data.getOtp().equals(otp);
    }

    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}