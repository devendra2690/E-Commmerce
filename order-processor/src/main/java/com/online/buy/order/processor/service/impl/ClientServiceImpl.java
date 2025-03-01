package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.Client;
import com.online.buy.common.code.repository.ClientRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client findById(Long ClientId) {
        return clientRepository.findById(ClientId)
                .orElseThrow(() -> new NotFoundException("Client does not exist"));
    }
}
