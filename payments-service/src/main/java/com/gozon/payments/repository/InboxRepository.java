package com.gozon.payments.repository;

import com.gozon.payments.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<Inbox, String> {
}
