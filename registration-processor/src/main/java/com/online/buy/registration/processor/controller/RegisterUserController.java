package com.online.buy.registration.processor.controller;


import com.online.buy.registration.processor.dto.UserRegistratiionDto;
import com.online.buy.registration.processor.entity.User;
import com.online.buy.registration.processor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class RegisterUserController {

    private final UserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistratiionDto request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }
}
