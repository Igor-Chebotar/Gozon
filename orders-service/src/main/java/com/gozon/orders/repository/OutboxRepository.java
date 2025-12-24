package com.gozon.orders.repository;

import com.gozon.orders.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, String> {
    List<Outbox> findBySentFalse();
}
