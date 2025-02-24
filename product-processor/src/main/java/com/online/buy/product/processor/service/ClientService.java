package com.online.buy.product.processor.service;

import com.online.buy.product.processor.model.ClientModel;

public interface ClientService {

    ClientModel registerClient(ClientModel clientModel);

    ClientModel findByClientId(Long clientId);

    ClientModel findById(Long clientId);

    void deleteClient(Long clientId);

    ClientModel updateClient(Long clientId, ClientModel client);
}
