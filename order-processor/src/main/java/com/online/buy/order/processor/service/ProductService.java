package com.online.buy.order.processor.service;

import com.online.buy.common.code.entity.Product;

public interface ProductService {
    Product findByProductIdAndClientId(Long productId,Long clientId);
}
