package com.glowapex.service;

import com.glowapex.dto.PaymentRequest;
import com.glowapex.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);

    List<PaymentResponse> getPaymentsByUser(Long userId);

    List<PaymentResponse> getAllPayments();
}