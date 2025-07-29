package com.online.buy.payment.processor.constant;

public class MessageBrokerConstants {
    // Naming constants for exchanges
    public static final String PAYMENT_PROCESSING_EXCHANGE = "payment-processing-exchange";
    public static final String PAYMENT_PROCESSING_DLQ_EXCHANGE = "payment-processing-dlq-exchange";
    public static final String PAYMENT_PROCESSING_RETRY_EXCHANGE = "payment-processing-retry-exchange";
    // Naming constants for queues
    public static final String PAYMENT_PROCESSING_QUEUE = "payment-processing-queue";
    public static final String PAYMENT_PROCESSING_DLQ = "payment-processing-dlq";
    public static final String PAYMENT_PROCESSING_RETRY_QUEUE = "payment-processing-retry-queue";
    // Naming constants for routing keys
    public static final String PAYMENT_PROCESSING_ROUTING_KEY = "payment.processing.routing.key";
    // Configuration for retry mechanism
    public static final Integer RETRY_DELAY_MS = 30000; // 30 seconds delay for retries
    // Arguments for dead-letter exchanges and queues
    public static final String DLQ_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String DLQ_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    public static final String MESSAGE_TTL = "x-message-ttl";



    // Naming constants for exchanges
    public static final String PAYMENT_PROCESSING_EXCHANGE_V1 = "payment-processing-v1-exchange";
    public static final String PAYMENT_PROCESSING_DLQ_EXCHANGE_V1 = "payment-processing-dlq-v1-exchange";
    // Naming constants for queues
    public static final String PAYMENT_PROCESSING_QUEUE_V1 = "payment-processing-v1-queue";
    public static final String PAYMENT_PROCESSING_DLQ_V1 = "payment-processing-v1-dlq";
    // Naming constants for routing keys
    public static final String PAYMENT_PROCESSING_ROUTING_KEY_V1 = "payment.processing.routing.v1.key";

}