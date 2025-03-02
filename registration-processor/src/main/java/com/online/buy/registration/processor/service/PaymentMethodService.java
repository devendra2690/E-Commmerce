package com.online.buy.registration.processor.service;

import com.online.buy.registration.processor.model.PaymentMethodModel;

public interface PaymentMethodService {
    void registerPaymentMethod(String UseId, PaymentMethodModel paymentMethodModel);
}
