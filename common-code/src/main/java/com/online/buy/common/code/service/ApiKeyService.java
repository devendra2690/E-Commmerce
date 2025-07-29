package com.online.buy.common.code.service;

import com.online.buy.common.code.entity.ApiKey;
import com.online.buy.common.code.repository.ApiKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final RateLimiterService rateLimiterService;

    public boolean isValidApiKey(String apiKey) {
        Optional<ApiKey> apiKeyData = apiKeyRepository.findByApiKey(apiKey);
        if (apiKeyData.isPresent() && apiKeyData.get().isActive()) {
            return rateLimiterService.isAllowed(apiKey, apiKeyData.get().getRateLimit());
        }
        return false;
    }
}
