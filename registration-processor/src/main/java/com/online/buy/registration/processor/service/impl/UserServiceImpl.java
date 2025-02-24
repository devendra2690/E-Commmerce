package com.online.buy.registration.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.entity.User;
import com.online.buy.registration.processor.enums.AccountStatus;
import com.online.buy.registration.processor.enums.Role;
import com.online.buy.registration.processor.repository.UserRepository;
import com.online.buy.registration.processor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // Hashing password
        user.setRole(Role.valueOf(role.toUpperCase()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setLastLogin(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public void deleteUser(String id) {
        User user =userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
