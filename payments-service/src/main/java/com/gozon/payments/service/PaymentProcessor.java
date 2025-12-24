package com.gozon.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.payments.dto.PaymentRequest;
import com.gozon.payments.dto.PaymentResult;
import com.gozon.payments.entity.Inbox;
import com.gozon.payments.entity.Outbox;
import com.gozon.payments.repository.InboxRepository;
import com.gozon.payments.repository.OutboxRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentProcessor {

    private final AccountService accountService;
    private final InboxRepository inboxRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    public PaymentProcessor(AccountService accountService, InboxRepository inboxRepo,
                           OutboxRepository outboxRepo, ObjectMapper mapper) {
        this.accountService = accountService;
        this.inboxRepo = inboxRepo;
        this.outboxRepo = outboxRepo;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.payment-request}")
    public void handlePaymentRequest(String msg) {
        try {
            PaymentRequest req = mapper.readValue(msg, PaymentRequest.class);
            processPayment(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void processPayment(PaymentRequest req) {
        if (inboxRepo.existsById(req.getOrderId())) {
            return;
        }

        Inbox inbox = new Inbox();
        inbox.setOrderId(req.getOrderId());
        inbox.setProcessed(true);
        inboxRepo.save(inbox);

        boolean ok = accountService.withdraw(req.getUserId(), req.getAmount());

        PaymentResult result;
        if (ok) {
            result = new PaymentResult(req.getOrderId(), true, "OK");
        } else {
            result = new PaymentResult(req.getOrderId(), false, "Not enough money");
        }

        try {
            Outbox outbox = new Outbox();
            outbox.setEventType("PAYMENT_RESULT");
            outbox.setPayload(mapper.writeValueAsString(result));
            outboxRepo.save(outbox);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
