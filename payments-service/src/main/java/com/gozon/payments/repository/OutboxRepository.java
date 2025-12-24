package com.gozon.payments.repository;

import com.gozon.payments.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, String> {
    List<Outbox> findBySentFalse();
}
