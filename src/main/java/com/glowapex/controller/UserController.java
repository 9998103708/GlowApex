package com.glowapex.controller;

import com.glowapex.dto.PasswordChangeRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @RequestBody PasswordChangeRequest request) {
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password updated successfully";
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats(@AuthenticationPrincipal User user) {
        List<Order> orders = orderRepository.findByUserId(user.getId());

        long activeOrders = orders.stream()
                .filter(o -> o != null && o.getStatus() == OrderStatus.PENDING)
                .count();

        long completedOrders = orders.stream()
                .filter(o -> o != null && o.getStatus() == OrderStatus.COMPLETED)
                .count();

        BigDecimal totalSpent = orders.stream()
                .map(o -> o != null && o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeOrders", activeOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("totalSpent", totalSpent);

        return stats;
    }
}