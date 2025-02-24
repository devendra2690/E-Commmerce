package com.online.buy.product.processor.repository;

import com.online.buy.product.processor.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByIdAndClientId(Long productId, Long clientId);
    List<Product> findByCategory(String category);
}
