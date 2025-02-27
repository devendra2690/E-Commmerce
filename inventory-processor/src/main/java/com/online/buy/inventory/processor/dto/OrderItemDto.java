package com.online.buy.inventory.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    @JsonProperty("productId")
    @NotNull(message = "ProductId can not be null")
    private Long productId;

    @JsonProperty("quantity")
    @Min(value = 1, message = "At least one quantity required")
    private int quantity;

    @JsonProperty("price")
    @DecimalMin(value = "1.0", inclusive = false, message = "Value should be greater than 1")
    private double price;
}

