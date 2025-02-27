package com.online.buy.registration.processor.service.impl;

import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class accessVerificationService {

    private final UserRepository userRepository;

    public boolean isResourceOwner(Authentication authentication, Long resourceId) {
        JwtAuthenticationToken jwtAuthenticationToken = ((JwtAuthenticationToken) authentication);
        jwtAuthenticationToken.getName();
        String clientId = (String) jwtAuthenticationToken.getTokenAttributes().get("client_id");
        if(clientId == null) {
            throw new AccessDeniedException(String.format("Client ID not configured for the user %s", authentication.getName()));
        }
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AccessDeniedException("User not found"));
        return user.getClient().getId().equals(resourceId);
    }
}
