package com.online.buy.registration.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.mapper.ConsumerMapper;
import com.online.buy.registration.processor.model.ConsumerModel;
import com.online.buy.registration.processor.service.ConsumerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final PasswordEncoder passwordEncoder;
    private final ConsumerRepository consumerRepository;

    @Override
    @Transactional
    public ConsumerModel registerConsumer(ConsumerModel consumerModel) {
        ConsumerEntity consumerEntity = new ConsumerEntity();
        ConsumerMapper.modelToEntityMapper(consumerModel, consumerEntity);
        consumerEntity.setCreatedAt(LocalDateTime.now());
        consumerEntity.setRegistrationDate(LocalDateTime.now());
        consumerEntity.setPasswordHash(passwordEncoder.encode(consumerModel.getPassword()));
        consumerEntity = consumerRepository.save(consumerEntity);
        return ConsumerMapper.entityToModelMapper(consumerEntity, new ConsumerModel());
    }

    @Override
    public ConsumerModel findConsumer(Long consumerId) {
        return ConsumerMapper.entityToModelMapper(findById(consumerId), new ConsumerModel());
    }

    @Override
    @Transactional
    public void deleteConsumer(Long consumerId) {
        consumerRepository.delete(findById(consumerId));
    }

    @Override
    @Transactional
    public ConsumerModel updateConsumer(Long consumerId, ConsumerModel consumerModel) {
        ConsumerEntity savedConsumerEntity = findById(consumerId);
        ConsumerMapper.modelToEntityMapper(consumerModel, savedConsumerEntity);
        savedConsumerEntity.setUpdatedAt(LocalDateTime.now());
        savedConsumerEntity.setLastLogin(LocalDateTime.now());
        return ConsumerMapper.entityToModelMapper(consumerRepository.save(savedConsumerEntity), new ConsumerModel());
    }

    private ConsumerEntity findById(Long consumerId) {
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new NotFoundException(String.format("Consumer with ID %s not found", consumerId)));
    }
}