package com.glowapex.controller;

import com.glowapex.dto.PaymentRequest;
import com.glowapex.entity.*;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    // ✅ Create a payment transaction
    @PostMapping
    public Payment createPayment(@RequestBody PaymentRequest request) {
        // Fetch user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Build payment object
        Payment payment = Payment.builder()
                .userEmail(user.getEmail())
                .order(order)
                .amount(request.getAmount())
                .source(request.getSource())
                .paymentMethod(request.getPaymentMethod())
                .status(request.getStatus() != null ? request.getStatus() : PaymentStatus.PENDING)
                .transactionId(request.getTransactionId())
                .currency(request.getCurrency())
                .build();

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Update order status based on payment status
        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (savedPayment.getStatus() == PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            order.setStatus(OrderStatus.PROCESSING);
        }
        orderRepository.save(order);

        return savedPayment;
    }

    // ✅ Get all payments for a specific user
    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentsByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paymentRepository.findByUserEmail(user.getEmail());
    }

    // ✅ Admin or SuperAdmin: Get all payments
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}