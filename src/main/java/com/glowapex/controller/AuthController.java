package com.glowapex.controller;

import com.glowapex.config.JwtUtil;
import com.glowapex.dto.AuthResponse;
import com.glowapex.dto.LoginRequest;
import com.glowapex.dto.PromoteRequest;
import com.glowapex.dto.RegisterRequest;
import com.glowapex.entity.User;
import com.glowapex.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword(), request.getRole());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword())
                .map(user -> new AuthResponse(jwtUtil.generateToken(user.getEmail())))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/promote")
    public User promoteToAdmin(@RequestBody PromoteRequest request) {
        return authService.promoteToAdmin(request.getEmail());
    }

    // Logout endpoint
    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "You have been logged out successfully.";
    }
}
