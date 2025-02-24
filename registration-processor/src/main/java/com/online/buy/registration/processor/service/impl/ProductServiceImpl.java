package com.online.buy.registration.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.entity.Product;
import com.online.buy.registration.processor.mapper.ProductMapper;
import com.online.buy.registration.processor.model.ProductModel;
import com.online.buy.registration.processor.repository.ProductRepository;
import com.online.buy.registration.processor.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductModel> listAll() {

        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products.stream().map(product -> ProductMapper.productToProductModel(product,new ProductModel())).toList();
    }

    @Override
    public ProductModel getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product %s not found", id)));
        return ProductMapper.productToProductModel(product, new ProductModel());
    }

    @Override
    public List<ProductModel> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(product -> ProductMapper.productToProductModel(product,new ProductModel())).toList();
    }
}
