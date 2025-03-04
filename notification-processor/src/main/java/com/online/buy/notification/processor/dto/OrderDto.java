package com.online.buy.notification.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("orderItems")
    private List<OrderItemDto> orderItemDto;
}
