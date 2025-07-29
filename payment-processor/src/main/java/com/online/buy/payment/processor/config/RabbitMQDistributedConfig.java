package com.online.buy.payment.processor.config;

import com.online.buy.payment.processor.constant.MessageBrokerConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDistributedConfig {

    // === EXCHANGES ===
    @Bean
    public DirectExchange paymentProcessingExchange() {
        return new DirectExchange(MessageBrokerConstants.PAYMENT_PROCESSING_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentProcessingDLQExchange() {
        return new DirectExchange(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentProcessingRetryExchange() {
        return new DirectExchange(MessageBrokerConstants.PAYMENT_PROCESSING_RETRY_EXCHANGE);
    }

    // === QUEUES ===
    @Bean
    public Queue paymentProcessingQueue() {

        // The main work queue. If a message fails, it's sent to the retry exchange.
        return QueueBuilder.durable(MessageBrokerConstants.PAYMENT_PROCESSING_QUEUE)
                .withArgument(MessageBrokerConstants.DLQ_LETTER_EXCHANGE, MessageBrokerConstants.PAYMENT_PROCESSING_RETRY_EXCHANGE)
                .withArgument(MessageBrokerConstants.DLQ_LETTER_ROUTING_KEY, MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue paymentRetryQueue() {

        // The retry queue. If a message fails again, it's sent to the DLQ.
        return QueueBuilder.durable(MessageBrokerConstants.PAYMENT_PROCESSING_RETRY_QUEUE)
                .withArgument(MessageBrokerConstants.DLQ_LETTER_EXCHANGE, MessageBrokerConstants.PAYMENT_PROCESSING_EXCHANGE)
                .withArgument(MessageBrokerConstants.DLQ_LETTER_ROUTING_KEY, MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY)
                .withArgument(MessageBrokerConstants.MESSAGE_TTL, MessageBrokerConstants.RETRY_DELAY_MS) // Retry after 30 seconds
                .build();
    }

    @Bean
    public Queue paymentProcessingDLQ() {
        // The dead-letter queue for messages that fail after retries.
        return QueueBuilder.durable(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ)
                .build();
    }

    // === BINDINGS ===
    @Bean
    public Binding paymentProcessingBinding() {
        return BindingBuilder.bind(paymentProcessingQueue()).to(paymentProcessingExchange())
                .with(MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY);
    }

    @Bean
    public Binding paymentRetryBinding() {
        return BindingBuilder.bind(paymentRetryQueue()).to(paymentProcessingRetryExchange())
                .with(MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY);
    }

    @Bean
    public Binding paymentDLQBinding() {
        return BindingBuilder.bind(paymentProcessingDLQ()).to(paymentProcessingDLQExchange())
                .with(MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY);
    }
}
