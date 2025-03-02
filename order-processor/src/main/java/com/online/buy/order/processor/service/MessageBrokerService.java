package com.online.buy.order.processor.service;

import com.online.buy.order.processor.entity.Order;

public interface MessageBrokerService {
    void processMessageToPaymentService(Order order);
}
