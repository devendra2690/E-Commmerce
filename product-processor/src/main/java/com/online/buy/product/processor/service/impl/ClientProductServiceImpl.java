package com.online.buy.product.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.product.processor.entity.Client;
import com.online.buy.product.processor.entity.Product;
import com.online.buy.product.processor.mapper.ProductMapper;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.repository.ClientRepository;
import com.online.buy.product.processor.repository.ProductRepository;
import com.online.buy.product.processor.service.ClientProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Override
    public ProductModel getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s", id)));
        return ProductMapper.productToProductModel(product, new ProductModel());
    }

    @Transactional
    @Override
    public ProductModel createProductForClient(Long clientId, ProductModel productModel) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException(String.format("Client not available with id: %s", clientId)));

        Product product = ProductMapper.productModelToProduct(productModel, new Product());
        product.setClient(client);
        productRepository.save(product);

        ProductMapper.productToProductModel(product, productModel);
        productModel.setClientId(clientId);

        return productModel;
    }

    @Transactional
    @Override
    public ProductModel updateProductForClient(Long clientId, Long productId, ProductModel productDetails) {

        Product product = productRepository.findByIdAndClientId(productId,clientId).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s and for Client %s", productId, clientId)));

        // Map only required fields
        ProductMapper.productModelToProduct(productDetails, product);
        productRepository.save(product);

        return ProductMapper.productToProductModel(product, productDetails);
    }

    @Transactional
    @Override
    public void deleteProduct(Long clientId, Long productId) {
        Product product = productRepository.findByIdAndClientId(productId,clientId).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s and for Client %s", productId, clientId)));
        productRepository.delete(product);
    }
}
