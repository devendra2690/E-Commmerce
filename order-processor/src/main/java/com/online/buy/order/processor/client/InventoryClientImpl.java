package com.online.buy.order.processor.client;

import com.online.buy.common.code.dto.inventory.InventoryItemClientDto;
import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.order.processor.config.ApplicationUrl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

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
    public InventoryClientDto validateInventories(@Valid InventoryClientDto inventoryClientDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<InventoryClientDto> entity = new HttpEntity<>(inventoryClientDto, headers);
        ResponseEntity<InventoryClientDto> response = restTemplate.exchange(
                applicationUrl.getInventoryValidate(),
                HttpMethod.POST,
                entity,
                InventoryClientDto.class
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
    public Map<Long, String> fallbackForValidateInventory(Exception e, InventoryItemClientDto inventoryItemClientDto) {
        System.out.println("All retries failed: " + e.getMessage());
        return Map.of(); // Return empty map as fallback
    }
}
