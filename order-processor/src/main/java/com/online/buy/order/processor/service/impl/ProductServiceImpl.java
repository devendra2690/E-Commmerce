package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.Product;
import com.online.buy.common.code.repository.ProductRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findByProductIdAndClientId(Long productId, Long clientId) {
        return productRepository.findByProductIdAndClientId(productId, clientId)
                .orElseThrow(() -> new NotFoundException("Invalid Product-Client combination"));
    }

    @Override
    public void updateInventory(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(String.format(" Product does not exist with id %s", productId)));
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}
