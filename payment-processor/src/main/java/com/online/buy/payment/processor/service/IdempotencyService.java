package com.online.buy.payment.processor.service;

import com.online.buy.payment.processor.constant.IdempotencyStatus;

/**
 * Service to ensure idempotency of operations.
 * In a production microservices architecture, this should be backed by a distributed
 * cache like Redis or a database table to share state across all instances.
 */
public interface IdempotencyService {

    /**
     * Attempts to start processing an operation. It checks if the operation is already
     * completed or currently in progress. If not, it marks it as 'IN_PROGRESS'.
     *
     * @param idempotencyKey A unique key identifying the operation.
     * @return The status indicating whether to proceed or skip.
     */
    IdempotencyStatus tryStartProcessing(String idempotencyKey);

    /**
     * Marks an operation as successfully completed.
     *
     * @param idempotencyKey A unique key identifying the operation.
     */
    void markAsCompleted(String idempotencyKey);
}
