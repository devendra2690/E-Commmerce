package com.online.buy.order.processor.client;

import com.online.buy.order.processor.client.dto.InventoryRequest;
import com.online.buy.order.processor.config.ApplicationUrl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class InventoryClientImpl implements InventoryClient{

    private final RestTemplate restTemplate;
    private final ApplicationUrl applicationUrl;

    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2),
            exceptionExpression = "#{#root instanceof T(java.net.SocketTimeoutException) || "
                    + "#root instanceof T(org.springframework.web.client.ResourceAccessException) || "
                    + "(#root instanceof T(org.springframework.web.client.HttpServerErrorException) && "
                    + "(#root.statusCode.value() == 503 || #root.statusCode.value() == 502))}"
    )
    public Map<Long, String> validateInventories(@Valid List<InventoryRequest> inventoryRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<InventoryRequest>> entity = new HttpEntity<>(inventoryRequest, headers);
        ResponseEntity<Map<Long, String>> response = restTemplate.exchange(
                applicationUrl.getInventoryValidate(),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<Long, String>>() {}
        );

        // ✅ **Handle HTTP response codes**
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            throw new HttpClientErrorException(response.getStatusCode(), "Client Error: " + response.getBody());
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Server Error: " + response.getBody());
        } else {
            throw new RuntimeException("Unexpected Response: " + response.getStatusCode());
        }
    }

    @Recover
    public Map<Long, String> fallbackForValidateInventory(Exception e, List<InventoryRequest> inventoryRequest) {
        System.out.println("All retries failed: " + e.getMessage());
        return Map.of(); // Return empty map as fallback
    }
}
