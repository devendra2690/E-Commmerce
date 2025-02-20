package com.online.buy.security_api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * Copyright (C) 2021 Montova BV. All rights reserved.
 *
 * @author Stijn Vandendael
 */
public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final static Logger LOGGER = LogManager.getLogger(JWTAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException {
        LOGGER.error(String.format("%s[%s]: %s", request.getMethod(), request.getRequestURI(), ex.getMessage()));
        LOGGER.warn(ex.getMessage());
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().println(String.format("%s -> %s", "You do not have the required permission to access this method", ex.getMessage()));
        httpServletResponse.getWriter().close();
    }
}
