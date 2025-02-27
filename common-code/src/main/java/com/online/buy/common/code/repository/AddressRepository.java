package com.online.buy.common.code.repository;


import com.online.buy.common.code.entity.AddressEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {
    @Query("SELECT a FROM AddressEntity a WHERE a.addressHash= :addressHash and a.user.id = :userId")
    Optional<AddressEntity> findByAddressHashAndUserId(@Param("addressHash") String addressHash, @Param("userId") UUID userId);
}
