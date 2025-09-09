package com.glowapex.service;

import com.glowapex.dto.AdminStatsResponse;
import com.glowapex.entity.OrderStatus;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AdminStatsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public AdminStatsResponse getStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        AdminStatsResponse stats = new AdminStatsResponse();

        stats.setTodayOrders(orderRepository.countByCreatedAtBetween(startOfDay, endOfDay));
        stats.setTodayPayments(paymentRepository.countByCreatedAtBetween(startOfDay, endOfDay));
        stats.setFailedOrders(orderRepository.countByStatus(OrderStatus.CANCELLED));
        stats.setInProgressOrders(orderRepository.countByStatus(OrderStatus.PROCESSING));
        stats.setCompletedOrders(orderRepository.countByStatus(OrderStatus.COMPLETED));
        stats.setRefundedOrders(orderRepository.countByStatus(OrderStatus.REFUNDED));

        return stats;
    }
}