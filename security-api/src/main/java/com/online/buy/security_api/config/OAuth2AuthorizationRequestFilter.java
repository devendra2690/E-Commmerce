package com.online.buy.security_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OAuth2AuthorizationRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Check if this is an OAuth2 authorization request
        if (request.getRequestURI().endsWith("/oauth2/authorize")) {
            // Extract the client_id from the request
            String clientId = request.getParameter("client_id");

            // Store the client_id in the request for later use
            request.setAttribute("client_id", clientId);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
