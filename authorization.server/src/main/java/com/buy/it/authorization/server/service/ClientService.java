package com.buy.it.authorization.server.service;

import com.buy.it.authorization.server.entity.Client;

import java.util.Set;

public interface ClientService {

    Client registerClient(String clientId, Set<String> redirectUrl);

    Client findByClientId(String clientId);

    Client findById(String id);

    void deleteClient(String id);

    Client updateClient(Client client);
}
