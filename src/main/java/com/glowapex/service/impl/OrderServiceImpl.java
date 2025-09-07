package com.glowapex.service.impl;

import com.glowapex.dto.OrderRequest;
import com.glowapex.dto.OrderResponse;
import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;
import com.glowapex.entity.*;
import com.glowapex.exception.UserNotFoundException;
import com.glowapex.mapper.OrderMapper;
import com.glowapex.mapper.PaymentMapper;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import com.glowapex.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        Order order = OrderMapper.toEntity(request);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        return OrderMapper.toResponse(orderRepository.save(order));
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

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = PaymentMapper.toEntity(request);
        payment.setOrder(order);
        payment.setUser(user);
        payment.setUserEmail(user.getEmail());

        Payment savedPayment = paymentRepository.save(payment);

        // Update order status based on payment result
        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (savedPayment.getStatus() == PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            order.setStatus(OrderStatus.PROCESSING);
        }
        orderRepository.save(order);

        return PaymentMapper.toResponse(savedPayment);
    }
}