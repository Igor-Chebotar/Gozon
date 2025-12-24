package com.gozon.orders.service;

import com.gozon.orders.entity.Outbox;
import com.gozon.orders.repository.OutboxRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboxProcessor {

    private final OutboxRepository outboxRepo;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing.payment-request}")
    private String paymentRequestRouting;

    public OutboxProcessor(OutboxRepository outboxRepo, RabbitTemplate rabbitTemplate) {
        this.outboxRepo = outboxRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutbox() {
        List<Outbox> events = outboxRepo.findBySentFalse();
        for (Outbox event : events) {
            try {
                rabbitTemplate.convertAndSend(exchange, paymentRequestRouting, event.getPayload());
                event.setSent(true);
                outboxRepo.save(event);
            } catch (Exception e) {
            }
        }
    }
}
