package com.online.buy.security_api.adapter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * Copyright (C) 2021 Montova BV. All rights reserved.
 *
 * @author Stijn Vandendael
 */
public interface SecurityConfigurationAdapter {

    List<RequestMatcher> customPublicRequestMatchers();

    boolean includeDefaultPublicRequestMatchers();

    List<AuthenticationProvider> customAuthenticationProviders();

    boolean includeDefaultAuthenticationProviders();

    AuthenticationFailureHandler customAuthenticationFailureHandler();
}
