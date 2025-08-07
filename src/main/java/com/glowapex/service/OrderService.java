package com.glowapex.service;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderRequest request);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Long id, OrderRequest request);

    void deleteOrder(Long id);

    List<Order> getOrdersByUserEmail(String email);
}
