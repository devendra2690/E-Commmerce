package com.online.buy.common.code.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InventoryClientDto {

    @JsonProperty("userId")
    @NotNull(message = "UserId can not be null")
    private String userId;

    @JsonProperty("orderItem")
    @Size(min = 1, message = "At least one item is required")
    @Valid
    private List<InventoryItemClientDto> inventoryItemClientDtos;
}
