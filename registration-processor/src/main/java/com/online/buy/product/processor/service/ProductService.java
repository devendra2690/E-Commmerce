package com.online.buy.product.processor.service;

import com.online.buy.product.processor.model.ProductModel;

import java.util.List;

public interface ProductService {
    List<ProductModel> listAll();
    ProductModel getProductById(Long id);
    List<ProductModel> getProductByCategory(String category);
}
