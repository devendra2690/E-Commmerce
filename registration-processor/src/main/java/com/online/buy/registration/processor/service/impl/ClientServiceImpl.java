package com.online.buy.registration.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.entity.Client;
import com.online.buy.registration.processor.entity.OAuth2Client;
import com.online.buy.registration.processor.mapper.ClientMapper;
import com.online.buy.registration.processor.model.ClientModel;
import com.online.buy.registration.processor.repository.ClientRepository;
import com.online.buy.registration.processor.repository.OAuth2ClientRepository;
import com.online.buy.registration.processor.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2ClientRepository oAuth2ClientRepository;

    @Transactional
    public ClientModel registerClient(ClientModel clientModel) {

        Client client = ClientMapper.clientModelToClientMapper(clientModel, new Client());
        clientRepository.save(client);

        OAuth2Client oAuth2Client = ClientMapper.clientModelToOAuthClientMapper(clientModel, new OAuth2Client());
        oAuth2Client.setGrantTypes(Set.of("authorization_code", "client_credentials"));
        oAuth2Client.setClientSecret(passwordEncoder.encode(clientModel.getClientSecret()));
        oAuth2Client.setScopes(Set.of("read", "write"));
        oAuth2Client.setClient(client);
        oAuth2ClientRepository.save(oAuth2Client);

        clientModel.setId(client.getId());
        return clientModel;
    }


    @Override
    public ClientModel findByClientId(Long clientId) {
/*
        Client client = clientRepository.findByClientId(clientId).orElseThrow(() -> new NotFoundException("Client not found"));
        return modelMapper.map(client, ClientModel.class);*/
        return null;
    }

    @Transactional
    @Override
    public ClientModel findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        return ClientMapper.clientToClientModelMapper(client, new ClientModel());
    }

    @Transactional
    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        oAuth2ClientRepository.deleteByClientId(client.getName());
        clientRepository.delete(client);
    }

    @Override
    public ClientModel updateClient(Long clientId, ClientModel client) {
        return null;
    }
}
