package com.online.buy.payment.processor.model;

import com.online.buy.payment.processor.enums.PaymentOptions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentOptionModel {
    private int numberOfInstallment;
    private BigDecimal amount;
    private double discount;
    private BigDecimal totalAmount;
    private PaymentOptions paymentOption;
}
