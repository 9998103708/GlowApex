package com.glowapex.controller;

import com.glowapex.dto.OrderRequest;
import com.glowapex.dto.OrderResponse;
import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;
import com.glowapex.entity.OrderStatus;
import com.glowapex.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{orderId}/payment")
    public PaymentResponse makePayment(@PathVariable Long orderId, @RequestBody PaymentRequest request) {
        return orderService.makePayment(orderId, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @PutMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long orderId,
                                           @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }
}