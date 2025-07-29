package com.online.buy.payment.processor.service;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentOptionService {
    List<?> getPaymentOptions(String userId, BigDecimal amount);
}
