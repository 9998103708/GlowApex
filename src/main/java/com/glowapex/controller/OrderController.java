package com.glowapex.controller;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.UserRepository;
import com.glowapex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(request.getEmail());
            String randomPassword = generateRandomPassword();
            user.setPassword(randomPassword); // Consider encoding if used for login
            user.setRole("USER");
            user = userRepository.save(user);
            emailService.sendCredentials(request.getEmail(), randomPassword);
        }

        Order order = new Order();
        order.setUser(user);
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        return orderRepository.save(order);
    }

    @PutMapping("/update-status/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public Order cancelOrder(@PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal(); // ✅ Correctly get the authenticated user

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Only ADMIN or the owner of the order can cancel
        if (!"ADMIN".equals(currentUser.getRole()) && !order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to cancel this order.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private String generateRandomPassword() {
        byte[] randomBytes = new byte[6];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
