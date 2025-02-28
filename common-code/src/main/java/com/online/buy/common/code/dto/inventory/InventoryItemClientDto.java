package com.online.buy.common.code.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InventoryItemClientDto {

    @JsonProperty("productId")
    @NotNull(message = "ProductId can not be null")
    private Long productId;

    @JsonProperty("reservationId")
    private Long reservationId;

    @JsonProperty("quantity")
    @Min(value = 1, message = "At least one quantity required")
    private int quantity;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;
}

