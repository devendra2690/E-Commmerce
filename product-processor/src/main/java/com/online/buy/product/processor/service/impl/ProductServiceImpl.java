package com.online.buy.product.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.product.processor.entity.Product;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.repository.ProductRepository;
import com.online.buy.product.processor.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductModel> listAll() {

        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products.stream().map(product -> modelMapper.map(product,ProductModel.class)).toList();
    }

    @Override
    public ProductModel getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product %s not found", id)));
        return modelMapper.map(product, ProductModel.class);
    }

    @Override
    public List<ProductModel> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(product -> modelMapper.map(product,ProductModel.class)).toList();
    }
}
