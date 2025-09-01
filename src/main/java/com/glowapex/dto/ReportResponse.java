package com.glowapex.dto;

import java.math.BigDecimal;

public class ReportResponse {
    private Long ordersReceived;
    private BigDecimal paymentsReceivedUSD;
    private BigDecimal paymentsReceivedINR;
    private Long failedOrders;
    private Long completedOrders;
    private Long inProgressOrders;
    private Long refundedOrders;
    private BigDecimal spentOnAPIs;
    private Long totalErrors;

    // Getters & Setters
    public Long getOrdersReceived() {
        return ordersReceived;
    }

    public void setOrdersReceived(Long ordersReceived) {
        this.ordersReceived = ordersReceived;
    }

    public BigDecimal getPaymentsReceivedUSD() {
        return paymentsReceivedUSD;
    }

    public void setPaymentsReceivedUSD(BigDecimal paymentsReceivedUSD) {
        this.paymentsReceivedUSD = paymentsReceivedUSD;
    }

    public BigDecimal getPaymentsReceivedINR() {
        return paymentsReceivedINR;
    }

    public void setPaymentsReceivedINR(BigDecimal paymentsReceivedINR) {
        this.paymentsReceivedINR = paymentsReceivedINR;
    }

    public Long getFailedOrders() {
        return failedOrders;
    }

    public void setFailedOrders(Long failedOrders) {
        this.failedOrders = failedOrders;
    }

    public Long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Long getInProgressOrders() {
        return inProgressOrders;
    }

    public void setInProgressOrders(Long inProgressOrders) {
        this.inProgressOrders = inProgressOrders;
    }

    public Long getRefundedOrders() {
        return refundedOrders;
    }

    public void setRefundedOrders(Long refundedOrders) {
        this.refundedOrders = refundedOrders;
    }

    public BigDecimal getSpentOnAPIs() {
        return spentOnAPIs;
    }

    public void setSpentOnAPIs(BigDecimal spentOnAPIs) {
        this.spentOnAPIs = spentOnAPIs;
    }

    public Long getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(Long totalErrors) {
        this.totalErrors = totalErrors;
    }
}