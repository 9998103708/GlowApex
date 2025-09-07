package com.glowapex.mapper;

import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;
import com.glowapex.entity.Payment;
import com.glowapex.entity.PaymentStatus;

public class PaymentMapper {

    public static Payment toEntity(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setSource(request.getSource());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.PENDING);
        return payment;
    }

    public static PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setTransactionId(payment.getTransactionId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setSource(payment.getSource());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        if (payment.getOrder() != null) {
            response.setOrderId(payment.getOrder().getId());
        }
        if (payment.getUser() != null) {
            response.setUserId(payment.getUser().getId());
        }
        response.setUserEmail(payment.getUserEmail());
        return response;
    }
}