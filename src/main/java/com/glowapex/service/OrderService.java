package com.glowapex.service;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import com.glowapex.entity.User;

import java.util.List;

public interface OrderService {
    Order placeOrder(OrderRequest request);

    List<Order> getOrdersByEmail(String email);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long orderId, OrderStatus status);

    Order cancelOrder(Long orderId, User currentUser);
}
