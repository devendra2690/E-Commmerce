package com.buy.it.authorization.server.controller;


import com.buy.it.authorization.server.dto.UserRegistrationRequest;
import com.buy.it.authorization.server.entity.User;
import com.buy.it.authorization.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }
}
