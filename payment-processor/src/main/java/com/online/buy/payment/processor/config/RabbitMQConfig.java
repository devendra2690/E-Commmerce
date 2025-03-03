package com.online.buy.payment.processor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MAIN_QUEUE = "payment.queue";
    public static final String DLQ = "payment.queue.dlq";
    public static final String DLQ_EXCHANGE = "payment.queue.dlq.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String RESERVATION_QUEUE = "reservation.queue";
    public static final String EXCHANGE = "order.exchange";

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE) // Route failed messages
                .withArgument("x-dead-letter-routing-key", DLQ) // DLQ Routing Key
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }

    @Bean
    public Queue reservationQueue() {
        return QueueBuilder.durable(RESERVATION_QUEUE).build();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with("order.process");
    }

    @Bean
    public Binding reservationBinding(Queue reservationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reservationQueue).to(exchange).with("reservation.process");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}

