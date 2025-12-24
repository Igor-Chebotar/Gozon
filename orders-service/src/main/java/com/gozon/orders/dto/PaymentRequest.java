package com.gozon.orders.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private String orderId;
    private String userId;
    private BigDecimal amount;

    public PaymentRequest() {}

    public PaymentRequest(String orderId, String userId, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
