package com.online.buy.registration.processor.repository;

import com.online.buy.registration.processor.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE c.oAuth2Client.clientId = :clientId")
    Optional<Client> findByClientId(@Param("clientId") String clientId);
}
