package com.online.buy.security_api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 *
 * JWTAuthenticationFailureHandler is a custom implementation of AuthenticationFailureHandler
 * that handles authentication failures by logging the error and sending an unauthorized response.
 * It logs the request method and URI along with the exception message, and sets the HTTP status to 401 Unauthorized.
 * The response body contains a message indicating that the user does not have the required permission to access the method.
 * This class is typically used in Spring Security configurations to handle authentication failures
 *
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
