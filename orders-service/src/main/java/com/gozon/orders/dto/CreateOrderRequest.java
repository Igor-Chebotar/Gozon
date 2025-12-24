package com.gozon.orders.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {
    private String userId;
    private BigDecimal amount;
    private String description;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
