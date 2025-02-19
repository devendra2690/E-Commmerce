package com.buy.it.authorization.server.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class OAuthExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthExceptionHandler.class);

    @ExceptionHandler(OAuth2AuthorizationException.class)
    public ResponseEntity<Map<String, Object>> handleOAuthErrors(OAuth2AuthorizationException ex) {
        LOGGER.error("OAuth Error: {}", ex.getError().getDescription());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getError().getErrorCode());
        response.put("error_description", ex.getError().getDescription());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationErrors(OAuth2AuthenticationException ex) {
        LOGGER.error("Authentication Error: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "authentication_failed");
        response.put("error_description", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalErrors(Exception ex) {
        LOGGER.error("Unexpected Error: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "internal_server_error");
        response.put("error_description", "Something went wrong. Please try again later.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
