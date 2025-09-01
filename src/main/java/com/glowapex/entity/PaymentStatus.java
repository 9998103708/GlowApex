package com.glowapex.entity;

public enum PaymentStatus {
    SUCCESS,        // Payment completed successfully
    FAILED,         // Payment failed
    PENDING,        // Payment is still pending
    REFUNDED,       // Payment refunded
    IN_PROGRESS     // Payment is being processed
}