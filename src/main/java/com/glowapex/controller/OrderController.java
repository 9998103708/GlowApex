package com.glowapex.controller;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.*;
import com.glowapex.repository.OrderRepository;
import com.glowapex.service.AuthService;
import com.glowapex.service.EmailService;
import com.glowapex.service.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final ServiceProductService serviceProductService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequest request) {
        // 1. Validate service
        ServiceProduct product = serviceProductService.findEntityByServiceName(request.getServiceName());
        if (product == null) {
            throw new RuntimeException("Selected service does not exist.");
        }

        // 2. Validate package
        ServicePackage matchedPackage = product.getPackages().stream()
                .filter(p -> p.getName().equalsIgnoreCase(request.getPackageName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selected package is invalid."));

        // 3. Validate quantity
        PackageQuantity matchedQuantity = matchedPackage.getQuantities().stream()
                .filter(q -> q.getAmount() == request.getQuantity())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selected quantity is not available."));

        // 4. Auto-create user if not found
        User user = authService.getUserByEmail(request.getEmail());
        if (user == null) {
            String rawPassword = generateRandomPassword();
            user = authService.register(request.getEmail(), rawPassword, "USER");
            emailService.sendCredentials(request.getEmail(), rawPassword);
        }

        // 5. Create order
        Order order = new Order();
        order.setUser(user);
        order.setServiceName(product.getServiceName());
        order.setPackageName(matchedPackage.getName());
        order.setQuantity(matchedQuantity.getAmount());
        order.setPrice(matchedQuantity.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setLink(request.getLink());

        return orderRepository.save(order);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myOrder")
    public List<Order> getUserOrders(@AuthenticationPrincipal User user) {
        return orderRepository.findByUserId(user.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allOrder")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-status/{orderId}")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/cancel/{orderId}")
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