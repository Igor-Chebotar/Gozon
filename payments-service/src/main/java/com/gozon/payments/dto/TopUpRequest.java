package com.gozon.payments.dto;

import java.math.BigDecimal;

public class TopUpRequest {
    private String userId;
    private BigDecimal amount;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
