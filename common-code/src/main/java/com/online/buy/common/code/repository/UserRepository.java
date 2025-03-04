package com.online.buy.common.code.repository;

import com.online.buy.common.code.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    List<User> findByIsEmailVerified(boolean isVerified);
}
