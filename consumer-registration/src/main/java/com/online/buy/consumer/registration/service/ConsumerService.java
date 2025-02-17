package com.online.buy.consumer.registration.service;

import com.online.buy.consumer.registration.domain.ConsumerModel;

public interface ConsumerService {
    ConsumerModel registerConsumer(ConsumerModel consumerModel);
    ConsumerModel findConsumer(long consumerId);
    void deleteConsumer(long consumerId);
}
