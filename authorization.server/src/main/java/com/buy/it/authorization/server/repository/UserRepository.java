package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(java.lang.String username);
}
