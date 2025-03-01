package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.UserRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
