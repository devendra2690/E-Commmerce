package com.online.buy.consumer.registration.config;

import com.online.buy.security_api.config.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityConfigurer implements SecurityConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/landing/**").permitAll()
                .anyRequest().authenticated()
        );
    }
}
