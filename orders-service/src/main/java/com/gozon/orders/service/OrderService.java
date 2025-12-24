package com.gozon.orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.dto.CreateOrderRequest;
import com.gozon.orders.dto.PaymentRequest;
import com.gozon.orders.entity.Order;
import com.gozon.orders.entity.OrderStatus;
import com.gozon.orders.entity.Outbox;
import com.gozon.orders.repository.OrderRepository;
import com.gozon.orders.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    public OrderService(OrderRepository orderRepo, OutboxRepository outboxRepo, ObjectMapper mapper) {
        this.orderRepo = orderRepo;
        this.outboxRepo = outboxRepo;
        this.mapper = mapper;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setAmount(request.getAmount());
        order.setDescription(request.getDescription());
        orderRepo.save(order);

        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(),
                order.getUserId(),
                order.getAmount()
            );
            Outbox outbox = new Outbox();
            outbox.setEventType("PAYMENT_REQUEST");
            outbox.setPayload(mapper.writeValueAsString(paymentRequest));
            outboxRepo.save(outbox);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create outbox event", e);
        }

        return order;
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepo.findByUserId(userId);
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepo.findById(orderId);
    }

    @Transactional
    public void updateOrderStatus(String orderId, boolean success) {
        orderRepo.findById(orderId).ifPresent(order -> {
            if (order.getStatus() == OrderStatus.NEW) {
                order.setStatus(success ? OrderStatus.FINISHED : OrderStatus.CANCELLED);
                orderRepo.save(order);
            }
        });
    }
}
