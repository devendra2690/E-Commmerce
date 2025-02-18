package com.online.buy.security_api.config;

import com.online.buy.security_api.filter.BuyItAuthenticationFilter;
import com.online.buy.security_api.filter.handlers.JWTAuthenticationFailureHandler;
import com.online.buy.security_api.filter.handlers.JWTAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(addCustomFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Enables basic authentication

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(adminUser);
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> customAuthenticationProviders,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        // Add custom providers to the list
        List<AuthenticationProvider> providers = new ArrayList<>(customAuthenticationProviders);

        return new ProviderManager(providers);  // List of custom authentication providers
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private BuyItAuthenticationFilter addCustomFilter() {
        BuyItAuthenticationFilter buyItAuthenticationFilter = new BuyItAuthenticationFilter("/rest/**");
        buyItAuthenticationFilter.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        buyItAuthenticationFilter.setAuthenticationSuccessHandler(new JWTAuthenticationSuccessHandler());
        return buyItAuthenticationFilter;
    }
}

