package com.online.buy.consumer.registration.service;

import com.online.buy.consumer.registration.dto.AuthenticationDto;

public interface AuthenticationService {
    void validateConsumer(AuthenticationDto authenticationDto);
}
