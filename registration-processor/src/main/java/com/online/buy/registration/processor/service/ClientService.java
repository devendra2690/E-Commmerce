package com.online.buy.registration.processor.service;

import com.online.buy.registration.processor.model.ClientModel;

public interface ClientService {

    ClientModel registerClient(ClientModel clientModel);

    ClientModel findByClientId(String clientId);

    ClientModel findById(Long clientId);

    void deleteClient(Long clientId);

    ClientModel updateClient(Long clientId, ClientModel client);
}
