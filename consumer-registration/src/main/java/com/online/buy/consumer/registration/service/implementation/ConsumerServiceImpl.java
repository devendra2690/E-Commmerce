package com.online.buy.consumer.registration.service.implementation;

import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.entity.ConsumerEntity;
import com.online.buy.consumer.registration.exception.DataNotFoundException;
import com.online.buy.consumer.registration.mapper.ConsumerMapper;
import com.online.buy.consumer.registration.repository.ConsumerRepository;
import com.online.buy.consumer.registration.service.ConsumerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final PasswordEncoder passwordEncoder;
    private final ConsumerRepository consumerRepository;

    public ConsumerServiceImpl(PasswordEncoder passwordEncoder
                                , ConsumerRepository consumerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.consumerRepository = consumerRepository;
    }

    @Override
    public ConsumerModel registerConsumer(ConsumerModel consumerModel) {
        ConsumerEntity consumerEntity = transformConsumerData(consumerModel);
        consumerRepository.save(consumerEntity);
        return ConsumerMapper.entityToModelMapper(consumerEntity, consumerModel);
    }

    @Override
    public ConsumerModel findConsumer(Long consumerId) {
        return ConsumerMapper.entityToModelMapper(findById(consumerId), new ConsumerModel());
    }

    @Override
    public void deleteConsumer(Long consumerId) {
        consumerRepository.delete(findById(consumerId));
    }

    @Override
    public ConsumerModel updateConsumer(Long consumerId, ConsumerModel consumerModel) {
        ConsumerEntity savedConsumerEntity = findById(consumerId);
        ConsumerMapper.modelToEntityMapper(consumerModel, savedConsumerEntity);
        savedConsumerEntity.setUpdatedAt(LocalDateTime.now());
        savedConsumerEntity.setLastLogin(LocalDateTime.now());
        consumerRepository.save(savedConsumerEntity);
        return ConsumerMapper.entityToModelMapper(savedConsumerEntity, consumerModel);
    }

    private ConsumerEntity transformConsumerData(ConsumerModel consumerModel) {
        ConsumerEntity consumerEntity = new ConsumerEntity();
        ConsumerMapper.modelToEntityMapper(consumerModel, consumerEntity);
        consumerEntity.setCreatedAt(LocalDateTime.now());
        consumerEntity.setRegistrationDate(LocalDateTime.now());

        String hashedPassword = passwordEncoder.encode(consumerModel.getPassword());
        consumerEntity.setPasswordHash(hashedPassword);

        return consumerEntity;
    }

    public ConsumerEntity findById(Long consumerId) {
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Consumer record not available for id %s", consumerId),
                        String.format("Consumer record not available for id %s", consumerId)));
    }
}