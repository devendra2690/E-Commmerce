package com.online.buy.security_api.adapter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * This interface allows customization of the security configuration
 * by providing hooks for public request matchers,
 * authentication providers,
 * and authentication failure handlers.
 *
 * Implementations of this interface can be used to modify the default security behavior
 * without changing the core security configuration.
 *
 */
public interface SecurityConfigurationAdapter {

    List<RequestMatcher> customPublicRequestMatchers();

    boolean includeDefaultPublicRequestMatchers();

    List<AuthenticationProvider> customAuthenticationProviders();

    boolean includeDefaultAuthenticationProviders();

    AuthenticationFailureHandler customAuthenticationFailureHandler();
}
