package com.glowapex.service;

import com.glowapex.dto.OrderRequest;
import com.glowapex.dto.OrderResponse;
import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getOrdersByUser(Long userId);

    List<OrderResponse> getAllOrders();

    PaymentResponse makePayment(Long orderId, PaymentRequest request);
}