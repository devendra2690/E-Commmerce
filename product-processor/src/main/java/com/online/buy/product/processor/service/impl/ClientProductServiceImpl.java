package com.online.buy.product.processor.service.impl;

import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.product.processor.entity.Client;
import com.online.buy.product.processor.entity.Product;
import com.online.buy.product.processor.mapper.ProductMapperConfig;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.repository.ClientRepository;
import com.online.buy.product.processor.repository.ProductRepository;
import com.online.buy.product.processor.service.ClientProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductModel getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s", id)));
        return modelMapper.map(product, ProductModel.class);
    }

    @Override
    public ProductModel createProductForClient(Long clientId, ProductModel productModel) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException(String.format("Client not available with id: %s", clientId)));

        Product product = modelMapper.map(productModel, Product.class);
        product.setClient(client);

        productRepository.save(product);

        productModel.setClientId(clientId);
        productModel.setId(product.getId());

        return productModel;
    }

    @Override
    public ProductModel updateProductForClient(Long clientId, Long productId, ProductModel productDetails) {

        Product product = productRepository.findByIdAndClientId(productId,clientId).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s and for Client %s", productId, clientId)));

       // Apply custom mapping configuration
        ProductMapperConfig.configure(modelMapper);
        // Map only required fields
        modelMapper.map(productDetails, product);

        productRepository.save(product);
        productDetails.setId(product.getId());

        return productDetails;
    }

    @Override
    public void deleteProduct(Long clientId, Long productId) {
        Product product = productRepository.findByIdAndClientId(productId,clientId).orElseThrow(() -> new NotFoundException(String.format("Product not available with id: %s and for Client %s", productId, clientId)));
        productRepository.delete(product);
    }
}
