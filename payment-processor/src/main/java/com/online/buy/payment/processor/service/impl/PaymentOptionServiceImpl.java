package com.online.buy.payment.processor.service.impl;

import com.online.buy.payment.processor.enums.PaymentOptions;
import com.online.buy.payment.processor.model.PaymentOptionModel;
import com.online.buy.payment.processor.service.PaymentOptionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentOptionServiceImpl implements PaymentOptionService {

    // Should be configured in DB
    private final double FULL_PAYMENT_DISCOUNT = 0.10;
    private final double HALF_YEAR_PAYMENT_DISCOUNT = 0.05;
    private final double QUARTERLY_PAYMENT_DISCOUNT = 0.02;

    @Override
    public List<?> getPaymentOptions(String userId, BigDecimal amount) {

        return List.of(
                new PaymentOptionModel(1, amount, FULL_PAYMENT_DISCOUNT, amount.subtract(amount.multiply(BigDecimal.valueOf(FULL_PAYMENT_DISCOUNT))), PaymentOptions.FULL_PAYMENT),
                new PaymentOptionModel(2, amount,HALF_YEAR_PAYMENT_DISCOUNT, amount.subtract(amount.multiply(BigDecimal.valueOf(HALF_YEAR_PAYMENT_DISCOUNT))), PaymentOptions.HALF_YEAR),
                new PaymentOptionModel(4, amount,QUARTERLY_PAYMENT_DISCOUNT, amount.subtract(amount.multiply(BigDecimal.valueOf(QUARTERLY_PAYMENT_DISCOUNT))), PaymentOptions.QUARTERLY_PAYMENT));

    }
}
