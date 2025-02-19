package com.buy.it.authorization.server.service;

import com.buy.it.authorization.server.entity.User;
import com.buy.it.authorization.server.enums.Role;
import com.buy.it.authorization.server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


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
