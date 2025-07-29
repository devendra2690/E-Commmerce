package com.online.buy.common.code.filter;

import com.online.buy.common.code.service.ApiKeyService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ApiKeyFilter implements Filter {

    private final ApiKeyService apiKeyService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String apiKey = httpRequest.getHeader("x-api-key");

        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
