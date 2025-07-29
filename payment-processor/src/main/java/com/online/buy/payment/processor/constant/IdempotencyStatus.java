package com.online.buy.payment.processor.constant;

public enum IdempotencyStatus {
    /**
     * The operation can proceed. This is a new request.
     */
    PROCEED,
    /**
     * The operation has already been completed successfully. Skip it.
     */
    SKIP_COMPLETED,
    /**
     * The operation is currently being processed by another thread/instance. Skip it.
     */
    SKIP_IN_PROGRESS
}
