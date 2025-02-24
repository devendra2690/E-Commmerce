package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.OAuth2Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2ClientRepository extends CrudRepository<OAuth2Client,Long> {
}
