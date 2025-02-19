package com.buy.it.authorization.server.service;

import com.buy.it.authorization.server.entity.User;

public interface UserService {
    boolean authenticate(String username, String password);

    User registerUser(String username, String email, String rawPassword, String role);

    User findByUsername(String username);

    User findById(String id);

    void deleteUser(String id);

    User updateUser(User user);
}
