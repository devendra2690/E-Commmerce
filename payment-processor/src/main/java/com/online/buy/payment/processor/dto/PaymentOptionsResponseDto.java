package com.online.buy.payment.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentOptionsResponseDto {
    private String userId;
    private List<?> paymentOptions;
}
