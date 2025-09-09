package com.glowapex.dto;

public class AdminStatsResponse {
    private long todayOrders;
    private long todayPayments;
    private long failedOrders;
    private long inProgressOrders;
    private long completedOrders;
    private long refundedOrders;

    // Getters & Setters
    public long getTodayOrders() {
        return todayOrders;
    }

    public void setTodayOrders(long todayOrders) {
        this.todayOrders = todayOrders;
    }

    public long getTodayPayments() {
        return todayPayments;
    }

    public void setTodayPayments(long todayPayments) {
        this.todayPayments = todayPayments;
    }

    public long getFailedOrders() {
        return failedOrders;
    }

    public void setFailedOrders(long failedOrders) {
        this.failedOrders = failedOrders;
    }

    public long getInProgressOrders() {
        return inProgressOrders;
    }

    public void setInProgressOrders(long inProgressOrders) {
        this.inProgressOrders = inProgressOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public long getRefundedOrders() {
        return refundedOrders;
    }

    public void setRefundedOrders(long refundedOrders) {
        this.refundedOrders = refundedOrders;
    }
}