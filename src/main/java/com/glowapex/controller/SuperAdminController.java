package com.glowapex.controller;

import com.glowapex.entity.Order;
import com.glowapex.entity.Payment;
import com.glowapex.entity.Role;
import com.glowapex.entity.User;
import com.glowapex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Only ADMIN / SUPERADMIN can view all admins
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userService.getAllAdmins();
    }

    // Only SUPERADMIN can promote
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping("/promote/{userId}")
    public User promoteToAdmin(@PathVariable Long userId) {
        return userService.promoteToAdmin(userId);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping("/demote/{adminId}")
    public User demoteToUser(@PathVariable Long adminId) {
        return userService.demoteToUser(adminId);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping("/create-superadmin")
    public User createSuperAdmin(@RequestParam String email,
                                 @RequestParam String password) {
        User existing = userService.findByEmail(email);
        if (existing != null) throw new RuntimeException("User already exists");

        User superAdmin = new User();
        superAdmin.setEmail(email);
        superAdmin.setPassword(passwordEncoder.encode(password));
        superAdmin.setRole(Role.SUPERADMIN);
        return userService.saveUser(superAdmin);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return userService.getAllOrders();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return userService.getAllPayments();
    }
}