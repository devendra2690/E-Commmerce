package com.online.buy.consumer.registration.service.implementation;

import com.online.buy.consumer.registration.dto.AuthenticationDto;
import com.online.buy.consumer.registration.repository.ConsumerRepository;
import com.online.buy.consumer.registration.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ConsumerRepository consumerRepository;

    public AuthenticationServiceImpl(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public void validateConsumer(AuthenticationDto authenticationDto) {

    }
}
