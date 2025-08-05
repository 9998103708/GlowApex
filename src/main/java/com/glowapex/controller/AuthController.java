package com.glowapex.controller;

import com.glowapex.config.JwtUtil;
import com.glowapex.dto.*;
import com.glowapex.entity.User;
import com.glowapex.service.AuthService;
import com.glowapex.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    // Register a user (used internally when placing an order too)
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword(), request.getRole());
    }

    // Login with email and password
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword())
                .map(user -> new AuthResponse(jwtUtil.generateToken(user.getEmail())))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    // Promote user to ADMIN role
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/promote")
    public User promoteToAdmin(@RequestBody PromoteRequest request) {
        return authService.promoteToAdmin(request.getEmail());
    }

    // Logout
    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "You have been logged out successfully.";
    }

    // Step 1: Request OTP for email
    @PostMapping("/request-otp")
    public String requestOtp(@RequestBody EmailRequest request) {
        otpService.generateAndSendOtp(request.getEmail());
        return "OTP has been sent to your email.";
    }

    // Step 2: Verify OTP and generate JWT token
    @PostMapping("/verify-otp")
    public AuthResponse verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (isValid) {
            return new AuthResponse(jwtUtil.generateToken(request.getEmail()));
        } else {
            throw new RuntimeException("Invalid or expired OTP");
        }
    }
}
