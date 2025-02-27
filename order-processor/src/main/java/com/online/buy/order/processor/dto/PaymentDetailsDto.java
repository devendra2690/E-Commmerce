package com.online.buy.order.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.online.buy.order.processor.enums.PaymentMode;
import com.online.buy.order.processor.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public class PaymentDetailsDto {

    @JsonProperty("paymentMode")
    @NotNull(message = "Payment Mode can not be null")
    private PaymentMode paymentMode;  // COD, UPI, Credit Card, etc.

    @JsonProperty("transactionId")
    private String transactionId;  // If paid online

    @JsonProperty("paymentStatus")
    @NotNull(message = "Payment status can not be null")
    private PaymentStatus paymentStatus;  // Paid or Pending
}
