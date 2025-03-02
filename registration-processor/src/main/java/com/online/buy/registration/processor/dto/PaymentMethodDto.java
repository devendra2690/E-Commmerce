package com.online.buy.registration.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentMethodDto {

    @JsonProperty("cardNumber")
    @NotNull(message = "Card Number can not be null")
    private String cardNumber;

    @JsonProperty("expMoth")
    @NotNull(message = "Expiry Month can not be null")
    private Long expMoth;

    @JsonProperty("expYear")
    @NotNull(message = "Expiry Year can not be null")
    private Long expYear;

    @JsonProperty("cvc")
    @NotNull(message = "CVC can not be null")
    private String cvc;
}
