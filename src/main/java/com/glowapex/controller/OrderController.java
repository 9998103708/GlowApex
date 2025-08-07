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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Place Order – Creates user if not exists
    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(request.getEmail());
            String rawPassword = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole("USER");
            user = userRepository.save(user);
            emailService.sendCredentials(request.getEmail(), rawPassword);
        }

        Order order = new Order();
        order.setUser(user);
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        return orderRepository.save(order);
    }

    // Logged-in USER can view their own orders
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myOrder")
    public List<Order> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        return orderRepository.findByUserId(currentUser.getId());
    }

    // Admin can view all orders
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allOrders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Admin can update order status
    @PutMapping("/update-status/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ✅ Authenticated USER or ADMIN can cancel their order
    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public Order cancelOrder(@PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) auth.getPrincipal();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"ADMIN".equals(currentUser.getRole()) && !order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to cancel this order.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    // ✅ Utility to generate a secure random password
    private String generateRandomPassword() {
        byte[] randomBytes = new byte[6];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}