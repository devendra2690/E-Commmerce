package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, String> {
    OAuth2Client findByClientId(String clientId);
}
