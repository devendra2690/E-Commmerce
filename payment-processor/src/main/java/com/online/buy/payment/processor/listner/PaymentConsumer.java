package com.online.buy.payment.processor.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.common.code.service.HmacServiceImpl;
import com.online.buy.payment.processor.constant.IdempotencyStatus;
import com.online.buy.payment.processor.constant.MessageBrokerConstants;
import com.online.buy.payment.processor.dto.PaymentMessageDto;
import com.online.buy.payment.processor.service.IdempotencyService;
import com.online.buy.payment.processor.service.PaymentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
    private static final int MAX_RETRIES = 3;
    private static final String RETRY_COUNT_HEADER = "x-retry-count";


    private final RabbitTemplate rabbitTemplate;
    private final PaymentService paymentService;
    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;
    private final EncryptionServiceImpl encryptionService;
    private final HmacServiceImpl hmacService;

    @RabbitListener(queues = MessageBrokerConstants.PAYMENT_PROCESSING_QUEUE) // Replace with constant from RabbitMQDistributedConfig
    public void handlePaymentMessage(Message message) {
        try {
            PaymentMessageDto paymentRequest = objectMapper.readValue(message.getBody(), PaymentMessageDto.class);

            log.info("Received Message: {}", message);

            /*// 1. DECRYPTION AND VALIDATION
            String decryptedCustomerId = encryptionService.decrypt(paymentRequest.getCustomerId());

            if (!hmacService.verify(paymentRequest.getCustomerId() + paymentRequest.getAmount(), paymentRequest.getSignature())) {
                throw new SecurityException("Invalid payment request: Possible attack detected!");
            }*/

            String idempotencyKey = getIdempotencyKey(message, paymentRequest);

            // 2. IDEMPOTENCY CHECK
            if (idempotencyService.tryStartProcessing(idempotencyKey) .equals(IdempotencyStatus.SKIP_COMPLETED)) {
                log.warn("Duplicate message detected. Ignoring payment with key: {}", idempotencyKey);
                // Acknowledge the message to remove it from the queue without processing.
                return;
            }

            // 3. PROCESS PAYMENT
            log.info("Processing payment with key: {}", idempotencyKey);
            //throw new RuntimeException("Simulated processing error for testing retry logic"); // Simulate an error for testing purposes
            paymentService.executePayment(paymentRequest, paymentRequest.getCustomerId());

            // If successful, the idempotency key is already stored from the check.
            // The message will be automatically acknowledged by Spring AMQP on successful exit.

        } catch (Exception e) {
            log.error("Processing failed for message. Reason: {}", e.getMessage());
            handleFailure(message);
        }
    }

    /**
     * Handles the logic for retrying or dead-lettering a failed message.
     */
    private void handleFailure(Message failedMessage) {
        long currentRetryCount = getRetryCount(failedMessage);

        if (currentRetryCount < MAX_RETRIES) {
            log.info("Sending message for retry. Attempt {} of {}", currentRetryCount + 1, MAX_RETRIES);

            // Create a new message with an incremented retry count in the header.
            Message messageToRetry = MessageBuilder.fromMessage(failedMessage)
                    .setHeader(RETRY_COUNT_HEADER, currentRetryCount + 1)
                    .build();

            // Re-publish to the retry exchange. It will be held in the retry queue for a TTL
            // before being routed back to the main work queue.
            rabbitTemplate.convertAndSend(MessageBrokerConstants.PAYMENT_PROCESSING_RETRY_EXCHANGE,
                    MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY, messageToRetry);
        } else {
            log.error("Max retries ({}) reached. Sending message to Dead Letter Queue.", MAX_RETRIES);
            // Re-publish to the DLX, which routes it to the final DLQ.
            rabbitTemplate.convertAndSend(MessageBrokerConstants.PAYMENT_PROCESSING_DLQ_EXCHANGE,
                    MessageBrokerConstants.PAYMENT_PROCESSING_ROUTING_KEY, failedMessage);
        }
    }

    /**
     * Extracts a unique key for idempotency checks.
     * Prefers the message's unique ID, but falls back to the paymentId from the payload.
     */
    private String getIdempotencyKey(Message message, PaymentMessageDto request) {
        String messageId = message.getMessageProperties().getMessageId();
        return messageId != null ? messageId : String.valueOf(request.getOrderId());
    }

    /**
     * CORRECTED: Gets the retry count from a custom header.
     */
    private long getRetryCount(Message message) {
        return message.getMessageProperties().getHeader(RETRY_COUNT_HEADER) != null
                ? message.getMessageProperties().getHeader(RETRY_COUNT_HEADER)
                : 0L;
    }
}
