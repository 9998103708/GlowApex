package com.glowapex.controller;

import com.glowapex.config.JwtUtil;
import com.glowapex.dto.*;
import com.glowapex.entity.User;
import com.glowapex.service.AuthService;
import com.glowapex.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    // Register a user (used internally when placing an order too)
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Login with email and password
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword())
                .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user.getEmail()))))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Invalid credentials")));
    }

    // Promote user to ADMIN role
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/promote")
    public ResponseEntity<User> promoteToAdmin(@RequestBody PromoteRequest request) {
        User user = authService.promoteToAdmin(request.getEmail());
        return ResponseEntity.ok(user);
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = Map.of("status", "success", "message", "You have been logged out successfully.");
        return ResponseEntity.ok(response);
    }

    // Step 1: Request OTP for email
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody EmailRequest request) {
        try {
            otpService.generateAndSendOtp(request.getEmail());
            log.info("OTP requested for: {}", request.getEmail());
            Map<String, String> response = Map.of(
                    "status", "success",
                    "message", "OTP has been sent to your email."
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to send OTP to {}: {}", request.getEmail(), e.getMessage());
            Map<String, String> response = Map.of(
                    "status", "error",
                    "message", "Failed to send OTP. Please try again."
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Step 2: Verify OTP and generate JWT token
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (isValid) {
            String token = jwtUtil.generateToken(request.getEmail());
            log.info("OTP verified successfully for: {}", request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            log.warn("Invalid or expired OTP attempt for: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid or expired OTP"));
        }
    }
}