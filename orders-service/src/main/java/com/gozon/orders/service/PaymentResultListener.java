package com.gozon.orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.dto.PaymentResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentResultListener {

    private final OrderService orderService;
    private final ObjectMapper mapper;

    public PaymentResultListener(OrderService orderService, ObjectMapper mapper) {
        this.orderService = orderService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.payment-result}")
    public void handlePaymentResult(String msg) {
        try {
            PaymentResult result = mapper.readValue(msg, PaymentResult.class);
            orderService.updateOrderStatus(result.getOrderId(), result.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
