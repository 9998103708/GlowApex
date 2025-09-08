package com.glowapex.mapper;

import com.glowapex.dto.OrderRequest;
import com.glowapex.dto.OrderResponse;
import com.glowapex.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setLink(request.getLink());
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setServiceName(order.getServiceName());
        response.setQuantity(order.getQuantity());
        response.setPrice(order.getPrice());
        response.setLink(order.getLink());
        response.setStatus(order.getStatus());
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