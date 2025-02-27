package com.online.buy.registration.processor.service;


import com.online.buy.common.code.entity.User;
import com.online.buy.registration.processor.dto.UserRegistrationDto;

public interface UserService {
    boolean authenticate(String username, String password);

    User registerUser(String username, String email, String rawPassword, String role, String clientId);

    User findByUsername(String username);

    User findById(String id);

    void deleteUser(String id);

    User updateUser(User user);

    User registerClientUser(Long clientId, UserRegistrationDto userRegistrationDto);
}
