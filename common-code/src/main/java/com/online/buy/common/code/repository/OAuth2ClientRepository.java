package com.online.buy.common.code.repository;

import com.online.buy.common.code.entity.OAuth2Client;
 import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2ClientRepository extends CrudRepository<OAuth2Client,Long> {

    @Modifying
    void deleteByClientId(String clientId);
    Optional<OAuth2Client> findByClientId(String clientId);
}
