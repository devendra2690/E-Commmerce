package com.online.buy.payment.processor.service;

import com.online.buy.payment.processor.constant.IdempotencyStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final String keyPrefix;
    private final long inProgressTimeoutMinutes;
    private final long completedExpirationHours;


    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_COMPLETED = "COMPLETED";


    public IdempotencyServiceImpl(
            StringRedisTemplate redisTemplate,
            @Value("${idempotency.redis.key-prefix}") String keyPrefix,
            @Value("${idempotency.redis.in-progress-timeout-minutes:5}") long inProgressTimeoutMinutes,
            @Value("${idempotency.redis.completed-expiration-hours:24}") long completedExpirationHours) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.inProgressTimeoutMinutes = inProgressTimeoutMinutes;
        this.completedExpirationHours = completedExpirationHours;
    }

    @Override
    public IdempotencyStatus tryStartProcessing(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency key cannot be null or empty.");
        }

        String redisKey = buildKey(idempotencyKey);

        // Atomically set the key to IN_PROGRESS only if it does not exist.
        // The timeout ensures that if the service crashes, the lock is eventually released.
        Boolean wasSet = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, STATUS_IN_PROGRESS, inProgressTimeoutMinutes, TimeUnit.MINUTES);

        if (Boolean.TRUE.equals(wasSet)) {
            // The key was successfully set. This is a new operation.
            return IdempotencyStatus.PROCEED;
        } else {
            // The key already exists. We need to check its status.
            String currentStatus = redisTemplate.opsForValue().get(redisKey);
            if (STATUS_COMPLETED.equals(currentStatus)) {
                return IdempotencyStatus.SKIP_COMPLETED;
            } else {
                return IdempotencyStatus.SKIP_IN_PROGRESS;
            }
        }
    }

    @Override
    public void markAsCompleted(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return; // Or throw an exception, depending on desired strictness.
        }
        String redisKey = buildKey(idempotencyKey);
        // Set the status to COMPLETED with a longer expiration.
        redisTemplate.opsForValue().set(redisKey, STATUS_COMPLETED, completedExpirationHours, TimeUnit.HOURS);
    }

    private String buildKey(String idempotencyKey) {
        return keyPrefix + ":" + idempotencyKey;
    }

}
