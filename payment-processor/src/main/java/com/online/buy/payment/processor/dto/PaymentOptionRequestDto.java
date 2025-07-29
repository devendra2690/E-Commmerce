package com.online.buy.payment.processor.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentOptionRequestDto {
    private String userId;
    private BigDecimal amount;
}
