package com.online.buy.registration.processor.service;


import com.online.buy.registration.processor.entity.User;

public interface UserService {
    boolean authenticate(String username, String password);

    User registerUser(String username, String email, String rawPassword, String role);

    User findByUsername(String username);

    User findById(String id);

    void deleteUser(String id);

    User updateUser(User user);
}
