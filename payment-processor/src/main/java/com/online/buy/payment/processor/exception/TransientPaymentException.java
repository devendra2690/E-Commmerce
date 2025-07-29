package com.online.buy.payment.processor.exception;

/**
 * Represents a transient error that is safe to retry.
 * For example, a network timeout or a temporary service unavailability.
 */
public class TransientPaymentException  extends RuntimeException {

    public TransientPaymentException(String message) {
        super(message);
    }
}
