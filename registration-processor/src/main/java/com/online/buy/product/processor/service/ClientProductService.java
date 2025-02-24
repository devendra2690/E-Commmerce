package com.online.buy.product.processor.service;

import com.online.buy.product.processor.model.ProductModel;

public interface ClientProductService {
    ProductModel getProductById(Long id);
    ProductModel createProductForClient(Long clientId, ProductModel product);
    ProductModel updateProductForClient(Long clientId, Long productId, ProductModel productDetails);
    void deleteProduct(Long clientId, Long productId);
}
