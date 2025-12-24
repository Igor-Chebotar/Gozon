package com.gozon.payments.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inbox")
public class Inbox {

    @Id
    @Column(name = "order_id")
    private String orderId;

    private boolean processed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Inbox() {
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
