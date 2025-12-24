package com.gozon.payments.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;

    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Long getVersion() { return version; }
}
