package com.online.buy.registration.processor.service;

import com.online.buy.registration.processor.model.ConsumerModel;

public interface ConsumerService {
    ConsumerModel registerConsumer(ConsumerModel consumerModel);
    ConsumerModel findConsumer(Long consumerId);
    void deleteConsumer(Long consumerId);
    ConsumerModel updateConsumer(Long consumerId, ConsumerModel consumerModel);
}
