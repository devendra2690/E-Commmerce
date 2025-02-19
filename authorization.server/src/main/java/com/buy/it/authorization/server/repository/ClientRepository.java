package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClientRepository extends CrudRepository<Client, UUID> {
}
