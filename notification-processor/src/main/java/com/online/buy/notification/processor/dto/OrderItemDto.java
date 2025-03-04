package com.online.buy.notification.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    @JsonProperty("itemName")
    private String itemName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total")
    private String total;
}
