package com.glowapex.controller;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.service.AuthService;
import com.glowapex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequest request) {
        User user = authService.getUserByEmail(request.getEmail());

        // If user doesn't exist → create & email credentials
        if (user == null) {
            String rawPassword = generateRandomPassword();
            user = authService.register(request.getEmail(), rawPassword, "USER");
            emailService.sendCredentials(request.getEmail(), rawPassword);
        }

        Order order = new Order();
        order.setUser(user);
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        return orderRepository.save(order);
    }

    // User can view own orders
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myOrder")
    public List<Order> getUserOrders(@AuthenticationPrincipal User user) {
        return orderRepository.findByUserId(user.getId());
    }

    // Admin can view all orders
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allOrder")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
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
    public Order cancelOrder(@AuthenticationPrincipal User currentUser,
                             @PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

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