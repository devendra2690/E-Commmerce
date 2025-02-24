package com.online.buy.product.processor.controller;

import com.online.buy.product.processor.dto.ProductDto;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.service.ClientProductService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class RegisterProduct {

    private final ClientProductService clientProductService;
    private ModelMapper modelMapper;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") @NotNull Long id) {
        return ResponseEntity.ok(modelMapper.map(clientProductService.getProductById(id), ProductDto.class));
    }

    @PostMapping("/{clientId}/product")
    public ResponseEntity<ProductDto> addProductToClient(@PathVariable Long clientId, @RequestBody ProductDto productDto) {
        ProductModel productModel = modelMapper.map(productDto, ProductModel.class);
        return ResponseEntity.ok(modelMapper.map(clientProductService.createProductForClient(clientId, productModel), ProductDto.class));
    }

    @PutMapping("/{clientId}/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("clientId") @NotNull Long clientId, @PathVariable("productId") @NotNull Long productId,
                                                    @RequestBody ProductDto productDetails) {
        ProductModel productModel = modelMapper.map(productDetails, ProductModel.class);
        return ResponseEntity.ok(modelMapper.map(clientProductService.updateProductForClient(clientId,productId, productModel), ProductDto.class));
    }

    @DeleteMapping("/{clientId}/product/{productId}")
    public ResponseEntity<Void> deRegisterProduct(@PathVariable("clientId") @NotNull Long clientId, @PathVariable("productId") @NotNull Long productId) {
        clientProductService.deleteProduct(clientId, productId);
        return ResponseEntity.noContent().build();
    }
}
