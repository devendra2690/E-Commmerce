package com.online.buy.consumer.registration.service.implementation;

import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.entity.ConsumerEntity;
import com.online.buy.consumer.registration.mapper.ObjectMapperUtil;
import com.online.buy.consumer.registration.repository.ConsumerRepository;
import com.online.buy.consumer.registration.service.ConsumerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ConsumerRepository consumerRepository;

    public ConsumerServiceImpl(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public ConsumerModel registerConsumer(ConsumerModel consumerModel) {
        ConsumerEntity consumerEntity = transformConsumerData(consumerModel);
        return null;
    }

    @Override
    public ConsumerModel findConsumer(long consumerId) {
        return null;
    }

    @Override
    public void deleteConsumer(long consumerId) {

    }

    private ConsumerEntity transformConsumerData(ConsumerModel consumerModel) {

        ConsumerEntity consumerEntity = ObjectMapperUtil.map(consumerModel, ConsumerEntity.class);
        consumerEntity.setCreatedAt(LocalDateTime.now());
        consumerEntity.setRegistrationDate(LocalDateTime.now());

        String hashedPassword = passwordEncoder.encode(consumerModel.getPassword());
        consumerEntity.setPasswordHash(hashedPassword);

        return consumerEntity;
    }
}
