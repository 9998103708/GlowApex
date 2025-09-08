package com.glowapex.service.impl;

import com.glowapex.dto.OrderRequest;
import com.glowapex.dto.OrderResponse;
import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;
import com.glowapex.entity.*;
import com.glowapex.mapper.OrderMapper;
import com.glowapex.mapper.PaymentMapper;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import com.glowapex.service.EmailService;
import com.glowapex.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create new order, auto-create user if not exists.
     */
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(request.getEmail());

            String rawPassword = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(Role.USER);

            user = userRepository.save(user);

            emailService.sendCredentials(request.getEmail(), rawPassword);
        }

        Order order = new Order();
        order.setUser(user);
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setLink(request.getLink());

        order = orderRepository.save(order);

        return OrderMapper.toResponse(order);
    }

    /**
     * Generate a random temporary password.
     */
    private String generateRandomPassword() {
        int length = 8;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse makePayment(Long orderId, PaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(order.getUser());
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setUserEmail(order.getUser().getEmail());
        payment.setSource(request.getSource());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);

        payment = paymentRepository.save(payment);

        return PaymentMapper.toResponse(payment);
    }
}