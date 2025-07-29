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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    /**
     * Configures the JwtDecoder to decode JWT tokens using the JWK Set URI.
     * This is necessary for validating JWT tokens issued by an OAuth2 authorization server.
     * The JWK Set URI is typically provided by the authorization server and contains the public keys used to verify the JWT signatures.
     *
     * @return a JwtDecoder instance configured with the JWK Set URI
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri("http://localhost:8080/oauth2/jwks")
                //.jwsAlgorithm(SignatureAlgorithm.RS256) Only needed if not fetching keys from Auth server and hardcoded somewhere
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

    /**
     * Configures the JwtAuthenticationConverter to use the "roles" claim for authorities.
     * This is necessary because Spring Security expects roles to be prefixed with "ROLE_".
     * The converter will map the "roles" claim to authorities without the "ROLE_" prefix.
     *
     * It takes the values from the roles array (e.g., ["ROLE_EDITOR", "ROLE_VIEWER"]) and creates a list of GrantedAuthority objects. Because you set the prefix to "",
     * it uses these names exactly as they are.
     *
     * This configuration is essential for ensuring that the JWT token's roles are correctly interpreted by Spring Security, allowing for proper authorization checks.
     *
     * @PreAuthorize("hasRole('EDITOR')") is called, Spring Security does the following:
     *
     * It looks at the Authentication object for the current user.
     *
     * It gets the list of their GrantedAuthority objects.
     *
     * The hasRole('EDITOR') expression automatically looks for an authority named ROLE_EDITOR.
     *
     * If it finds a match in the user's authority list, access is granted. If not, a 403 Forbidden error is returned.
     *
     * This is why it's crucial to ensure that the JwtAuthenticationConverter is set up correctly to map the JWT roles to Spring Security's expected format.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 🛠 Explicitly set the claim name to "roles" instead of "scope"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        // 🛠 Ensure roles have "ROLE_" prefix (Spring Security requires it)
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {

            List<RequestMatcher> requestMatchers = new ArrayList<>();
            if (montovaSecurityConfigAdapter.isPresent()) {
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

            }
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

