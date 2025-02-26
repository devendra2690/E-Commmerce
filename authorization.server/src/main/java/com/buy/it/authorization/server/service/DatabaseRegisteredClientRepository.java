package com.buy.it.authorization.server.service;

import com.buy.it.authorization.server.entity.OAuth2Client;
import com.buy.it.authorization.server.repository.OAuth2ClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class DatabaseRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2ClientRepository oAuth2ClientRepository;

    public DatabaseRegisteredClientRepository(OAuth2ClientRepository oAuth2ClientRepository) {
        this.oAuth2ClientRepository = oAuth2ClientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("Saving clients is not supported dynamically.");
    }

    @Override
    public RegisteredClient findById(String id) {
        return oAuth2ClientRepository.findById(Long.parseLong(id))
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return oAuth2ClientRepository.findByClientId(clientId)
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    private RegisteredClient mapToRegisteredClient(OAuth2Client client) {
        return RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientName(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(client.getRedirectUris().stream().findFirst().orElseThrow())
                .postLogoutRedirectUri("http://127.0.0.1:8082/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
    }
}

