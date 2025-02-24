package com.online.buy.product.processor.controller;

import com.online.buy.product.processor.dto.ProductDto;
import com.online.buy.product.processor.mapper.ProductMapper;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.service.ClientProductService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class RegisterProductController {

    private final ClientProductService clientProductService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") @NotNull Long id) {
        return ResponseEntity.ok(ProductMapper.productModelToProductDto(clientProductService.getProductById(id), new ProductDto()));
    }

    @PostMapping("/{clientId}/product")
    public ResponseEntity<ProductDto> addProductToClient(@PathVariable Long clientId, @RequestBody ProductDto productDto) {
        ProductModel productModel = ProductMapper.productDtoToProductModel(productDto, new ProductModel());
        return ResponseEntity.ok(ProductMapper.productModelToProductDto(clientProductService.createProductForClient(clientId, productModel),productDto));
    }

    @PutMapping("/{clientId}/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("clientId") @NotNull Long clientId, @PathVariable("productId") @NotNull Long productId,
                                                    @RequestBody ProductDto productDetails) {
        ProductModel productModel =  ProductMapper.productDtoToProductModel(productDetails, new ProductModel());
        return ResponseEntity.ok(ProductMapper.productModelToProductDto(clientProductService.updateProductForClient(clientId,productId, productModel), productDetails));
    }

    @DeleteMapping("/{clientId}/product/{productId}")
    public ResponseEntity<Void> deRegisterProduct(@PathVariable("clientId") @NotNull Long clientId, @PathVariable("productId") @NotNull Long productId) {
        clientProductService.deleteProduct(clientId, productId);
        return ResponseEntity.noContent().build();
    }
}
