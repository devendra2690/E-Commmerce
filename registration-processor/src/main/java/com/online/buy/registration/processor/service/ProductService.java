package com.online.buy.registration.processor.service;

import com.online.buy.registration.processor.model.ProductModel;

import java.util.List;

public interface ProductService {
    List<ProductModel> listAll();
    ProductModel getProductById(Long id);
    List<ProductModel> getProductByCategory(String category);
}
