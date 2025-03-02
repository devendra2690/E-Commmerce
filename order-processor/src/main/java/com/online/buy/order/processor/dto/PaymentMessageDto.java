package com.online.buy.order.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMessageDto {

    @JsonProperty("customerId")
    @NotNull(message = "Customer Id  can not be null")
    private String customerId;

    @JsonProperty("amount")
    @NotNull(message = "Amount cannot be null")
    private Long amount;

    @JsonProperty("signature")
    @NotNull(message = "signature cannot be null")
    private String signature;
}
