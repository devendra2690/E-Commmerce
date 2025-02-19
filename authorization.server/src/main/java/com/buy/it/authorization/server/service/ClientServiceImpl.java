package com.buy.it.authorization.server.service;

import com.buy.it.authorization.server.entity.Client;
import com.buy.it.authorization.server.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client registerClient(String clientId, Set<String> redirectUris) {

        Client client = Client.builder()
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode("your-client-secret")) // Hash client secret
                .grantTypes(Set.of("authorization_code", "client_credentials"))
                .redirectUris(redirectUris)
                .scopes(Set.of("read", "write"))
                .build();

        return clientRepository.save(client);
    }


    @Override
    public Client findByClientId(String clientId) {
        return null;
    }

    @Override
    public Client findById(String id) {
        return null;
    }

    @Override
    public void deleteClient(String id) {

    }

    @Override
    public Client updateClient(Client client) {
        return null;
    }
}
