package com.gozon.payments.service;

import com.gozon.payments.entity.Outbox;
import com.gozon.payments.repository.OutboxRepository;
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

    @Value("${app.rabbitmq.routing.payment-result}")
    private String paymentResultRouting;

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
                rabbitTemplate.convertAndSend(exchange, paymentResultRouting, event.getPayload());
                event.setSent(true);
                outboxRepo.save(event);
            } catch (Exception e) {
            }
        }
    }
}
