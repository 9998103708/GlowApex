package com.glowapex.controller;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.*;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ Create a new order
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .serviceName(request.getServiceName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .link(request.getLink())   // ✅ added link
                .status(OrderStatus.PENDING) // default status
                .build();

        return orderRepository.save(order);
    }

    // ✅ Get all orders for a user
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(userId); // ✅ fixed to userId
    }

    // ✅ Admin or SuperAdmin: Get all orders
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Create a payment for an existing order
    @PostMapping("/{orderId}/payment")
    public Payment makePayment(@PathVariable Long orderId, @RequestBody Payment paymentRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .userEmail(order.getUser().getEmail())
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .source(paymentRequest.getSource())
                .status(paymentRequest.getStatus() != null ? paymentRequest.getStatus() : PaymentStatus.PENDING)
                .transactionId(paymentRequest.getTransactionId())
                .currency(paymentRequest.getCurrency())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        }

        return savedPayment;
    }
}