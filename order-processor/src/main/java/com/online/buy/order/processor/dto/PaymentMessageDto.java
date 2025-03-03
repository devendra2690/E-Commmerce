package com.online.buy.order.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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

    @JsonProperty("orderId")
    @NotNull(message = "OderId cannot be null")
    private Long orderId;

    @JsonProperty("reservationId")
    private List<Long> reservationId;
}
