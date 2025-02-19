package com.online.buy.security_api.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityConfigurer {
    void configure(HttpSecurity http) throws Exception;
}
