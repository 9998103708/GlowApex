package com.glowapex.controller;

import com.glowapex.dto.AdminStatsResponse;
import com.glowapex.dto.UserProfileDTO;
import com.glowapex.service.AdminStatsService;
import com.glowapex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminStatsService adminStatsService;

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping
    public AdminStatsResponse getAdminStats() {
        return adminStatsService.getStats();
    }

    // ADMIN + SUPERADMIN can view all users
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/users")
    public List<UserProfileDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}