package com.online.buy.registration.processor.controller;


import com.online.buy.common.code.entity.User;
import com.online.buy.registration.processor.dto.UserRegistrationDto;
import com.online.buy.registration.processor.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class RegisterUserController {

    private final UserService userService;

    @PostMapping("/register-user")
        public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), "BUYER", "consumer-app");
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }

    @PostMapping("/register-app-user")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<String> registerAppUser(@RequestBody @Valid UserRegistrationDto request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getRole(), request.getClientId());
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }

    @PostMapping("/client/{clientId}/register-user")
    @PreAuthorize("hasAnyRole('SELLER') and @accessVerificationService.isResourceOwner(authentication, #clientId)")
    public ResponseEntity<String> registerClientUser(@PathVariable("clientId") @NotNull Long clientId, @RequestBody @Valid UserRegistrationDto request) {
        if(!request.getRole().equalsIgnoreCase("SELLER")) {
            throw new IllegalArgumentException(String.format("%s role can be registered by only ADMIN and SUPER ADMIN",request.getRole()));
        }
        User user = userService.registerClientUser(clientId,request);
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }
}
