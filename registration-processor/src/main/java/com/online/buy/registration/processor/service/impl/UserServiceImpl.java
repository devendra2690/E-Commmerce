package com.online.buy.registration.processor.service.impl;

import com.online.buy.registration.processor.entity.User;
import com.online.buy.registration.processor.enums.Role;
import com.online.buy.registration.processor.repository.UserRepository;
import com.online.buy.registration.processor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(String username, String password) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPasswordHash()); // Validate password
        }
        return false;
    }

    @Override
    public User registerUser(String username, String email, String rawPassword, String role) {

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword)) // Hashing password
                .role(Role.valueOf(role.toUpperCase()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
