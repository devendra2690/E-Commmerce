package com.online.buy.product.processor.controller;

import com.online.buy.product.processor.dto.ProductDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class RegisterProduct {

    public void addProduct(Long clientId, ProductDto productDto) {
        // Add product to the database

    }

    public void updateProduct(Long clientId, ProductDto productDto) {
        // Update product in the database

    }

    public void deRegisterProduct(Long clientId, Long productId) {
        // Remove product from the database

    }
}
