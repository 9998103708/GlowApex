package com.glowapex.repository;

import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔹 Find all orders for a specific user
    List<Order> findByUserId(Long userId);

    // 🔹 Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // 🔹 Find orders for a user by status
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    // 🔹 Count orders by status
    long countByStatus(OrderStatus status);

    // 🔹 Count orders in a date range
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 🔹 Count orders by status in a given range
    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.status = :status " +
            "AND o.createdAt >= :startDate " +
            "AND o.createdAt < :endDate")
    long countOrdersByStatusInRange(@Param("status") OrderStatus status,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}