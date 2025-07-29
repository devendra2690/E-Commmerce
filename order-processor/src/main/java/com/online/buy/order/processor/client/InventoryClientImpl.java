package com.online.buy.order.processor.client;

import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.order.processor.config.ApplicationUrl;
import com.online.buy.order.processor.exception.InventoryClientException;
import com.online.buy.order.processor.exception.InventoryServiceUnavailableException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class InventoryClientImpl implements InventoryClient{

    private final RestTemplate restTemplate;
    private final ApplicationUrl applicationUrl;

    private static final Logger logger = LoggerFactory.getLogger(InventoryClientImpl.class);


    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2),
            exceptionExpression = "#root instanceof T(com.online.buy.order.processor.exception.InventoryServiceUnavailableException)"
    )
    public InventoryClientDto validateInventories(@Valid InventoryClientDto inventoryClientDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<InventoryClientDto> entity = new HttpEntity<>(inventoryClientDto, headers);
            ResponseEntity<InventoryClientDto> response = restTemplate.exchange(
                    applicationUrl.getInventoryValidate(),
                    HttpMethod.POST,
                    entity,
                    InventoryClientDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() == null) {
                    logger.error("Inventory service returned empty body");
                    throw new InventoryServiceUnavailableException("Inventory service returned empty body");
                }
                return response.getBody();
            } else if (response.getStatusCode().is4xxClientError()) {
                logger.error("Client Error: {}", response.getBody());
                throw new InventoryClientException("Client Error: " + response.getStatusCode());
            } else if (response.getStatusCode().is5xxServerError()) {
                logger.error("Server Error: {}", response.getBody());
                throw new InventoryServiceUnavailableException("Server Error: " + response.getStatusCode());
            } else {
                logger.error("Unexpected Response: {}", response.getStatusCode());
                throw new InventoryServiceUnavailableException("Unexpected Response: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error while validating inventories: {}", e.getMessage(), e);
            throw new InventoryServiceUnavailableException("Error while validating inventories", e);
        }
    }

    @Recover
    public InventoryClientDto fallbackForValidateInventory(Exception e, InventoryClientDto inventoryClientDto) {
        logger.error("All retries failed for validateInventories: {}", e.getMessage(), e);
        throw new InventoryServiceUnavailableException("Inventory service is currently unavailable. Please try again later.");
    }
}
