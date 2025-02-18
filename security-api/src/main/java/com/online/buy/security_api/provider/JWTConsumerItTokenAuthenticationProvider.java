package com.online.buy.security_api.provider;

import com.online.buy.security_api.token.JWTConsumerAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JWTConsumerItTokenAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JWTConsumerAuthenticationToken jwtConsumerAuthenticationToken = (JWTConsumerAuthenticationToken) authentication;

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTConsumerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
