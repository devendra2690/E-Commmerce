package com.online.buy.order.processor.service;

public interface MessageBrokerService {
    void sendPaymentRequest(String customerId, Long amount) throws Exception;
}
