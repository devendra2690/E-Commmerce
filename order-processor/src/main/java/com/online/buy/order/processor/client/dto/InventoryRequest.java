package com.online.buy.order.processor.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;
}
