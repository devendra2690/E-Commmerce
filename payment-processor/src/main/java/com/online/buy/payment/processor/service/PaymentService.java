package com.online.buy.payment.processor.service;

import com.online.buy.payment.processor.dto.PaymentMessageDto;

public interface PaymentService {
    void executePayment(PaymentMessageDto paymentMessageDto, String custId);
}
