package com.glowapex.service;

import com.glowapex.entity.User;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public User register(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");
        return userRepository.save(user);
    }

    public User promoteToAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ================== OTP Login Methods ===================

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    public boolean sendOtpToUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = generateOTP();

            // Set expiry to 5 minutes from now
            LocalDateTime expiryTime = Instant.now()
                    .plusSeconds(5 * 60)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            user.setOtp(otp);
            user.setOtpExpiry(expiryTime);
            userRepository.save(user);

            emailService.sendOtpEmail(email, otp);

            return true;
        }
        return false;
    }

    public Optional<User> loginWithOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getOtp() != null && user.getOtpExpiry() != null) {
                long nowMillis = Instant.now().toEpochMilli();
                long expiryMillis = user.getOtpExpiry()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();

                if (otp.equals(user.getOtp()) && nowMillis <= expiryMillis) {
                    user.setOtp(null);
                    user.setOtpExpiry(null);
                    userRepository.save(user);
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }
}