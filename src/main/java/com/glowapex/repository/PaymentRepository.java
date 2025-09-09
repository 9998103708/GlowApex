package com.glowapex.repository;

import com.glowapex.entity.Payment;
import com.glowapex.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 🔹 Find payments by user email
    List<Payment> findByUserEmail(String email);

    // 🔹 Find payments for an order
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    List<Payment> findByOrderId(@Param("orderId") Long orderId);

    // 🔹 Find payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // 🔹 Count by status in a date range
    long countByStatusAndCreatedAtBetween(PaymentStatus status, LocalDateTime start, LocalDateTime end);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 🔹 Sum amount by status & currency in a date range
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.status = :status " +
            "AND p.currency = :currency " +
            "AND p.createdAt BETWEEN :start AND :end")
    Double sumAmountByStatusAndCurrency(@Param("status") PaymentStatus status,
                                        @Param("currency") String currency,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}