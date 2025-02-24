package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
}
