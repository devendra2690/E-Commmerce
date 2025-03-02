package com.online.buy.registration.processor.service.impl;

import com.online.buy.common.code.entity.Client;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.enums.AccountStatus;
import com.online.buy.common.code.enums.Role;
import com.online.buy.common.code.repository.ClientRepository;
import com.online.buy.common.code.repository.UserRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.dto.UserRegistrationDto;
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
    private final ClientRepository clientRepository;

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
    public User registerUser(String username, String email, String rawPassword, String role, String clientId) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // Hashing password
        user.setRole(Role.valueOf(role.toUpperCase()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setLastLogin(LocalDateTime.now());

        Client client = clientRepository.findByClientId(clientId).orElseThrow(() -> new NotFoundException("Client not found"));
        user.setClient(client);

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
        if(user == null || user.getId() == null) {
            // thro exception
            throw new NotFoundException("User not found");
        }
        return userRepository.save(user);
    }

    @Override
    public User registerClientUser(Long clientId, UserRegistrationDto userRegistrationDto) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException("Client not found"));
        if(!client.getOAuth2Client().getClientId().equals(userRegistrationDto.getClientId())) {
            throw new IllegalArgumentException("Client ID mismatch. Please provide correct client ID");
        }

        return registerUser(userRegistrationDto.getUsername(), userRegistrationDto.getEmail(), userRegistrationDto.getPassword()
                , userRegistrationDto.getRole(), userRegistrationDto.getClientId());
    }
}
