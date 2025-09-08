package com.glowapex.mapper;

import com.glowapex.dto.OrderResponse;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setServiceName(order.getServiceName());
        response.setQuantity(order.getQuantity());
        response.setPrice(order.getPrice());
        response.setLink(order.getLink());
        // ✅ Default status to PENDING if null
        response.setStatus(order.getStatus() != null ? order.getStatus() : OrderStatus.PENDING);
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        if (order.getUser() != null) {
            response.setUserId(order.getUser().getId());
            response.setUserEmail(order.getUser().getEmail());
        }

        if (order.getPayments() != null) {
            response.setPayments(order.getPayments().stream()
                    .map(PaymentMapper::toResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }
}