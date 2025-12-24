package com.gozon.payments.dto;

public class PaymentResult {
    private String orderId;
    private boolean success;
    private String message;

    public PaymentResult() {}

    public PaymentResult(String orderId, boolean success, String message) {
        this.orderId = orderId;
        this.success = success;
        this.message = message;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
