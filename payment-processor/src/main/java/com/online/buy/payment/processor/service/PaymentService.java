package com.online.buy.payment.processor.service;

import com.online.buy.payment.processor.dto.PaymentMessageDto;

public interface PaymentService {
    void chargeCustomer(PaymentMessageDto paymentMessageDto, String custId);
}
