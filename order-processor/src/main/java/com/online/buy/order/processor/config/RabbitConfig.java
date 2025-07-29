package com.online.buy.order.processor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String EXCHANGE = "order.exchange";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue paymentQueue() {
        return QueueBuilder.durable(PAYMENT_QUEUE).build();
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentQueue).to(exchange).with("payment.process");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);

        // Publisher confirm callback
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                System.out.println("Message confirmed: " + correlationData);
            } else {
                System.err.println("Message not confirmed: " + cause);
            }
        });

        // Return callback for unroutable messages
        rabbitTemplate.setReturnsCallback(returned -> {
            System.err.println("Returneda message: " + returned.getMessage());
        });

        return rabbitTemplate;
    }
}
