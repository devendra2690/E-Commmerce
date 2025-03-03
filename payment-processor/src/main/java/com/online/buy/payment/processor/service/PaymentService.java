package com.online.buy.payment.processor.service;

public interface PaymentService {
    void chargeCustomer(String customerId, Long amount);
}
