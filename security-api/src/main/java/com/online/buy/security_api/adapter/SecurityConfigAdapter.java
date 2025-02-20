package com.online.buy.security_api.adapter;

import com.online.buy.security_api.handler.JWTAuthenticationFailureHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;
import java.util.List;

public abstract class SecurityConfigAdapter implements SecurityConfigurationAdapter {

    /**
     * Custom list of request matchets
     * Default: empty list
     * @return
     */
    public List<RequestMatcher> customPublicRequestMatchers() {
        return Collections.emptyList();
    }

    /**
     * Should the default public request matchers be added?
     * Default: true
     * @return
     */
    public boolean includeDefaultPublicRequestMatchers() {
        return true;
    }

    /**
     * Custom list of authentication providers
     * Default: empty list
     * @return
     */
    public List<AuthenticationProvider> customAuthenticationProviders() {
        return Collections.emptyList();
    }

    /**
     * Include the default authentication providers
     * Default: true
     * @return
     */
    public boolean includeDefaultAuthenticationProviders() {
        return true;
    }

    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new JWTAuthenticationFailureHandler();
    }
}
