package com.glowapex.controller;

import com.glowapex.dto.UserProfileDTO;
import com.glowapex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // ADMIN + SUPERADMIN can view stats
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getAllUsersCount());
        stats.put("totalOrders", userService.getAllOrdersCount());
        stats.put("totalPayments", userService.getAllPaymentsCount());
        return stats;
    }

    // ADMIN + SUPERADMIN can view all users
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/users")
    public List<UserProfileDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}