package com.online.buy.authentication.processor.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/register").permitAll()  // Allow registration endpoint without authentication
                        .anyRequest().authenticated()  // Secure all other requests
                )
                .csrf(csrf -> csrf.disable());  // Disable CSRF (if required)

        return http.build();
    }
}


