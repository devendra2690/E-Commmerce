package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.OAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomRegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private final OAuth2ClientRepository clientRepository;

    public CustomRegisteredClientRepositoryImpl(OAuth2ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        // Implement if you want to allow dynamic client registration
    }

    @Override
    public RegisteredClient findById(String id) {
        OAuth2Client client = clientRepository.findById(id).orElseThrow();
        return toRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OAuth2Client client = clientRepository.findByClientId(clientId);
        return toRegisteredClient(client);
    }

    private RegisteredClient toRegisteredClient(OAuth2Client client) {

        // Convert comma-separated strings to sets
        Set<ClientAuthenticationMethod> clientAuthenticationMethods = Arrays.stream(client.getClientAuthenticationMethods().split(","))
                .map(ClientAuthenticationMethod::new)
                .collect(Collectors.toSet());

        Set<AuthorizationGrantType> authorizationGrantTypes = Arrays.stream(client.getAuthorizationGrantTypes().split(","))
                .map(AuthorizationGrantType::new)
                .collect(Collectors.toSet());

        Set<String> redirectUris = Arrays.stream(client.getRedirectUris().split(","))
                .collect(Collectors.toSet());

        Set<String> scopesList = Arrays.stream(client.getScopes().split(","))
                .collect(Collectors.toSet());

        return RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethods(methods -> methods.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(types -> types.addAll(authorizationGrantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .scopes(scopes -> scopes.addAll(scopesList))
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(client.getRequireAuthorizationConsent())
                        .build())
                .build();
    }
}