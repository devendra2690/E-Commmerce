package com.online.buy.common.code.repository;

import com.online.buy.common.code.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByIdAndClientId(Long productId, Long clientId);
    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.id= :productId and p.client.id = :clientId")
    Optional<Product> findByProductIdAndClientId(@Param("productId") Long productId, @Param("clientId") Long clientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p where p.id = :productId")
    Optional<Product> findQuantityByProductId(@Param("productId") Long productId);
}
