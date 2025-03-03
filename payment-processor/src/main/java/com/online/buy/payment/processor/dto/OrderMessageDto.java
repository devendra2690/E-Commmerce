package com.online.buy.payment.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderMessageDto {

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("status")
    private String status;
}
