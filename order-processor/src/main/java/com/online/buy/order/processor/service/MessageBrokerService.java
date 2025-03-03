package com.online.buy.order.processor.service;

import java.util.List;

public interface MessageBrokerService {
    void sendPaymentRequest(String customerId, Long amount, Long orderId, List<Long> reservationId) throws Exception;
}
