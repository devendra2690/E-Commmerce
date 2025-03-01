package com.online.buy.order.processor.service;

import com.online.buy.common.code.entity.Client;

public interface ClientService {
    Client findById(Long ClientId);
}
