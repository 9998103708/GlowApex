package com.glowapex.service;

import com.glowapex.entity.PaymentStatus;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Map<String, Object> getReport(String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;

        switch (type.toLowerCase()) {
            case "day":
                start = now.truncatedTo(ChronoUnit.DAYS);
                break;
            case "week":
                start = now.minusWeeks(1);
                break;
            case "month":
                start = now.minusMonths(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type: " + type);
        }

        Map<String, Object> report = new HashMap<>();

        // ✅ Order stats
        report.put("totalOrders", orderRepository.countByCreatedAtBetween(start, now));
        report.put("totalCompletedOrders", paymentRepository.countByStatusAndCreatedAtBetween(PaymentStatus.SUCCESS, start, now));
        report.put("totalFailedOrders", paymentRepository.countByStatusAndCreatedAtBetween(PaymentStatus.FAILED, start, now));
        report.put("totalRefundedOrders", paymentRepository.countByStatusAndCreatedAtBetween(PaymentStatus.REFUNDED, start, now));
        report.put("totalOrdersInProgress", paymentRepository.countByStatusAndCreatedAtBetween(PaymentStatus.IN_PROGRESS, start, now));

        // ✅ Payment stats
        report.put("paymentReceivedUSD", paymentRepository.sumAmountByStatusAndCurrency(PaymentStatus.SUCCESS, "USD", start, now));
        report.put("paymentReceivedINR", paymentRepository.sumAmountByStatusAndCurrency(PaymentStatus.SUCCESS, "INR", start, now));

        // ✅ TODO: integrate external API spend & error logs if available
        report.put("totalSpentOnAPIs", 0.0);
        report.put("totalErrors", 0);

        return report;
    }
}