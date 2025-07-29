package com.online.buy.payment.processor.config;

import com.online.buy.payment.processor.constant.MessageBrokerConstants;
import com.online.buy.payment.processor.exception.TransientPaymentException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

@Configuration
public class DeclarativeRabbitMQDistributedConfig {

    // === EXCHANGES ===
    @Bean
    public DirectExchange paymentProcessingExchangeV1() {
        return new DirectExchange(MessageBrokerConstants.PAYMENT_PROCESSING_EXCHANGE_V1);
    }

    @Bean
    public DirectExchange paymentProcessingDLQExchangeV1() {
        return new DirectExchange(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ_EXCHANGE_V1);
    }

    // === QUEUES ===
    @Bean
    public Queue paymentProcessingQueueV1() {

        // The main work queue. If a message fails, it's sent to the retry exchange.
        return QueueBuilder.durable(MessageBrokerConstants.PAYMENT_PROCESSING_QUEUE_V1)
                .build();
    }

    @Bean
    public Queue paymentProcessingDLQV1() {
        // The dead-letter queue for messages that fail after retries.
        return QueueBuilder.durable(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ_V1)
                .build();
    }

    // === BINDINGS ===
    @Bean
    public Binding paymentProcessingBindingV1() {
        return BindingBuilder.bind(paymentProcessingQueueV1()).to(paymentProcessingExchangeV1())
                .with(MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY_V1);
    }

    @Bean
    public Binding paymentDLQBindingV1() {
        return BindingBuilder.bind(paymentProcessingDLQV1()).to(paymentProcessingDLQExchangeV1())
                .with(MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY_V1);
    }


    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor(RabbitTemplate rabbitTemplate, Jackson2JsonMessageConverter jsonMessageConverter) {

        rabbitTemplate.setMessageConverter(jsonMessageConverter);

        RetryTemplate retryTemplate = getRetryTemplate();

        // This interceptor will apply the retry logic defined above.
        RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);

        // When all retries fail, the message is "recovered".
        // Here, we configure it to be sent to our DLX.
        interceptor.setRecoverer((message, cause) -> {

            Message dlqMessage = MessageBuilder.fromMessage((Message) message[1])
                    .setHeader("x-error-cause", cause.getCause().getMessage()) // Custom header to indicate death
                    .build();

            rabbitTemplate.convertAndSend(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ_EXCHANGE_V1, MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY_V1, dlqMessage);
            return null;
        });

        return interceptor;
    }

    /***
     * Configures a RetryTemplate with an exponential backoff policy and a retry policy
     * that only retries on TransientPaymentException. You can customize the initial interval, Multiplier, and max interval
     * Also configures the retry policy to retry a maximum of 3 times.and exception types to retry.
     * This is used by the RetryOperationsInterceptor to apply retry logic to RabbitMQ listeners.
     * You can adjust the retry count and backoff settings as needed.
     * This configuration is used in the RabbitListenerContainerFactory to apply retry logic declaratively.
     * This allows you to apply retry logic to all RabbitMQ listeners without needing to manually handle retries in each listener method.
     */

    private static RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 1. Configure the Backoff Strategy (Exponential Backoff)
        ExponentialRandomBackOffPolicy exponentialRandomBackOffPolicy = new ExponentialRandomBackOffPolicy();
        exponentialRandomBackOffPolicy.setInitialInterval(5000); // 5 seconds
        exponentialRandomBackOffPolicy.setMultiplier(2.0); // Exponential backoff
        exponentialRandomBackOffPolicy.setMaxInterval(60000); // 30 seconds max interval
        retryTemplate.setBackOffPolicy(exponentialRandomBackOffPolicy);

        // 2. Configure the Retry Policy (Specific Exceptions)
        // This policy will retry up to 3 times ONLY for TransientPaymentException.
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = Map.of(
                TransientPaymentException.class, true // Retry on this exception
        );

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, retryableExceptions, true);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        return retryTemplate;
    }

    /**
     * Overrides the default listener container factory to add our custom retry interceptor.
     * This ensures that any @RabbitListener will automatically have the retry logic applied.
     */
    @Bean("declarativeRetryListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RetryOperationsInterceptor retryInterceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // Add our powerful retry interceptor to the listener's advice chain.
        factory.setAdviceChain(retryInterceptor);

        return factory;
    }
}
