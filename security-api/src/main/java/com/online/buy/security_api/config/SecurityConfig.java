package com.online.buy.security_api.config;


import com.online.buy.security_api.adapter.SecurityConfigurationAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final static Logger LOGGER = LogManager.getLogger(SecurityConfig.class);


    private final Optional<SecurityConfigurationAdapter> montovaSecurityConfigAdapter;

    public SecurityConfig(Optional<SecurityConfigurationAdapter> montovaAuthenticationProviderAdapter) {
        this.montovaSecurityConfigAdapter = montovaAuthenticationProviderAdapter;
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri("http://localhost:8080/oauth2/jwks")
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                 .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Prefix roles

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        System.out.println("webSecurityCustomizer() bregin");

        return (web) -> {

            List<RequestMatcher> requestMatchers = new ArrayList<>();
            System.out.println("webSecurityCustomizer() Started");
            if (montovaSecurityConfigAdapter.isPresent()) {
                System.out.println("webSecurityCustomizer() Present");


                final SecurityConfigurationAdapter montovaSecurityConfigurationAdapter = this.montovaSecurityConfigAdapter.get();
                if (montovaSecurityConfigurationAdapter.includeDefaultPublicRequestMatchers()) {
                    getDefaultPublicRequestMatchers().forEach(e -> logConfiguring(e, "default"));
                    requestMatchers.addAll(getDefaultPublicRequestMatchers());
                }
                montovaSecurityConfigurationAdapter.customPublicRequestMatchers().forEach(e -> logConfiguring(e, "custom"));
                requestMatchers.addAll(montovaSecurityConfigurationAdapter.customPublicRequestMatchers());
            } else {
                getDefaultPublicRequestMatchers().forEach(e -> logConfiguring(e, "default"));
                requestMatchers.addAll(getDefaultPublicRequestMatchers());
                System.out.println("webSecurityCustomizer() not Present");

            }

            System.out.println("requestMatchers() requestMatchers "+requestMatchers.toArray());

            web.ignoring().requestMatchers("/ignore1", "/ignore2");
            web.ignoring().requestMatchers(requestMatchers.toArray(new RequestMatcher[0]));
        };
    }


    /*@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        System.out.println("✅ WebSecurityCustomizer is being applied!");
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }*/

    protected List<RequestMatcher> getDefaultPublicRequestMatchers() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        requestMatchers.add(new AntPathRequestMatcher("/error"));
        requestMatchers.add(new AntPathRequestMatcher("/health-check/**"));
        requestMatchers.add(new AntPathRequestMatcher("/swagger-ui.html"));
        requestMatchers.add(new AntPathRequestMatcher("/swagger-ui/**"));
        requestMatchers.add(new AntPathRequestMatcher("/swagger-resources/**"));
        requestMatchers.add(new AntPathRequestMatcher("/v3/api-docs/**"));
        requestMatchers.add(new AntPathRequestMatcher("/ws/**"));
        return requestMatchers;
    }

    private void logConfiguring(RequestMatcher requestMatcher, String type) {
        LOGGER.info(String.format("Configuring %s public available request matcher '%s'", type, requestMatcher.toString()));
    }
}

