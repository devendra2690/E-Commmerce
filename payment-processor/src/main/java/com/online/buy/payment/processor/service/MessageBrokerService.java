package com.online.buy.payment.processor.service;

import java.util.List;

public interface MessageBrokerService {

    void sendOrderProcessMessage(Long orderId, List<Long> reservationId, String status);
}
