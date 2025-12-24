package com.gozon.orders.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.queue.payment-request}")
    private String paymentRequestQueue;

    @Value("${app.rabbitmq.queue.payment-result}")
    private String paymentResultQueue;

    @Value("${app.rabbitmq.routing.payment-request}")
    private String paymentRequestRouting;

    @Value("${app.rabbitmq.routing.payment-result}")
    private String paymentResultRouting;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue paymentRequestQueue() {
        return QueueBuilder.durable(paymentRequestQueue).build();
    }

    @Bean
    public Queue paymentResultQueue() {
        return QueueBuilder.durable(paymentResultQueue).build();
    }

    @Bean
    public Binding paymentRequestBinding() {
        return BindingBuilder.bind(paymentRequestQueue()).to(exchange()).with(paymentRequestRouting);
    }

    @Bean
    public Binding paymentResultBinding() {
        return BindingBuilder.bind(paymentResultQueue()).to(exchange()).with(paymentResultRouting);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
