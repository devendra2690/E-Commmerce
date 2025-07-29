package com.online.buy.payment.processor.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.buy.payment.processor.constant.MessageBrokerConstants;
import com.online.buy.payment.processor.dto.PaymentMessageDto;
import com.online.buy.payment.processor.exception.PermanentPaymentException;
import com.online.buy.payment.processor.exception.TransientPaymentException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The updated service now throws specific, custom exceptions to allow for
 * intelligent retry decisions by the messaging infrastructure.
 */
@Service
@AllArgsConstructor
public class DeclarativePaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeclarativePaymentConsumer.class);
    private final ObjectMapper objectMapper;


    @RabbitListener(
            queues = MessageBrokerConstants.PAYMENT_PROCESSING_QUEUE_V1,
            containerFactory = "declarativeRetryListenerContainerFactory"
    )
    public void executePayment(Message message) {

        PaymentMessageDto paymentRequest = null;
        try {
            paymentRequest = objectMapper.readValue(message.getBody(), PaymentMessageDto.class);
        } catch (IOException e) {
            throw new PermanentPaymentException("Failed to parse payment request message.", e);
        }

        log.info("Executing payment for ID: {}, Amount: {} {}",
                paymentRequest.getCustomerId(),
                paymentRequest.getOrderId(),
                paymentRequest.getAmount());

        // Simulate a permanent failure (e.g., invalid data)
        if (paymentRequest.getAmount().doubleValue() < 0) {
            throw new PermanentPaymentException("Payment amount cannot be negative.");
        }

        // Simulate a transient network error that should be retried
        if (2222L == paymentRequest.getOrderId()) {
            throw new TransientPaymentException("Simulated transient network failure to payment gateway.");
        }

        log.info("Payment successful for ID: {}", paymentRequest.getOrderId());
    }
}
