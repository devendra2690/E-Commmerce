package com.online.buy.common.code.service;

import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;

@Service
public class RateLimiterService {
    private final RedisTemplate<String, Integer> redisTemplate;

    public RateLimiterService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String apiKey, int maxRequestsPerSecond) {
        String key = "rate_limit:" + apiKey;
        Integer requests = redisTemplate.opsForValue().get(key);

        if (requests == null) {
            redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(1));
            return true;
        }

        if (requests < maxRequestsPerSecond) {
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        return false;
    }
}
