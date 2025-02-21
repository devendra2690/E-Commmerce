package com.buy.it.authorization.server.repository;

import com.buy.it.authorization.server.entity.OAuthKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthKeyRepository extends JpaRepository<OAuthKey, Long> {

    @Query("SELECT k FROM OAuthKey k ORDER BY k.id DESC LIMIT 1")
    OAuthKey findLatestKey();
}

