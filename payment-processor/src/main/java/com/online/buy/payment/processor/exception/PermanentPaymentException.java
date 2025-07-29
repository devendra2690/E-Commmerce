package com.online.buy.payment.processor.exception;

/**
 * Represents a permanent error that should NOT be retried.
 * For example, invalid input data or a business rule violation.
 * The message will be sent directly to the DLQ.
 */
public class PermanentPaymentException extends RuntimeException {

    public PermanentPaymentException(String message) {
        super(message);
    }

    public PermanentPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
