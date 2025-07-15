package com.glowapex.controller;

import com.glowapex.dto.PaymentRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.Payment;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/make")
    public Payment makePayment(@RequestBody PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        return paymentRepository.save(payment);
    }
}