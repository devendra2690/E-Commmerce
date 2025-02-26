/*


   We can write custom Provider like to validate user along with Client id:
   Purpose here is:

   When we start auth code flow, OAuth only validate if Client registered or not, and then validate user credentials,
   It does not validate if user is allowed to access this client or not.

   This is not most optimized way to do this, but this is just to show how we can do this.

   Here we need to find good way to fetch client id use for auth code flow.

   When implement custom login page and custom auth endpoint it's very easy to do this. We can pass client id as hidden parameter in login form.







package com.buy.it.authorization.server.provider;

import com.buy.it.authorization.server.entity.OAuth2Client;
import com.buy.it.authorization.server.entity.User;
import com.buy.it.authorization.server.repository.OAuth2ClientRepository;
import com.buy.it.authorization.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final OAuth2ClientRepository oAuth2ClientRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = (UsernamePasswordAuthenticationToken) authentication;

        // Get HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String[] clientIds = request.getParameterValues("client_id");

        if (clientIds == null || clientIds.length == 0) {
            throw new BadCredentialsException("No client_id found");
        }

        String clientId = clientIds[0]; // Take first client_id from request

        OAuth2Client oAuth2Client = oAuth2ClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new BadCredentialsException("Invalid client_id"));

        Long clientDbId = Optional.ofNullable(oAuth2Client.getClient())
                .map(client -> client.getId())
                .orElseThrow(() -> new BadCredentialsException("Client mapping error"));

        User user = userRepository.findByClientIdAndUsername(clientDbId, authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("User is not allowed to authenticate with this client."));

         // Use passcode to validate password here

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        return new UsernamePasswordAuthenticationToken(user, authRequest.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
*/
