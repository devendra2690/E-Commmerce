package com.online.buy.registration.processor.controller;

import com.online.buy.registration.processor.dto.ProductDto;
import com.online.buy.registration.processor.mapper.ProductMapper;
import com.online.buy.registration.processor.model.ProductModel;
import com.online.buy.registration.processor.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductInformrationController {

    private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> listAllProducts() {
        List<ProductModel> productModels = productService.listAll();
       return ResponseEntity.ok(productModels.stream().map(productModel -> ProductMapper.productModelToProductDto(productModel, new ProductDto())).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") @NotNull Long productId) {
       ProductModel productModel = productService.getProductById(productId);
       return ResponseEntity.ok(ProductMapper.productModelToProductDto(productModel, new ProductDto()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable("category") @NotNull String category) {
        List<ProductModel> productModels = productService.getProductByCategory(category);
        return ResponseEntity.ok(productModels.stream().map(productModel -> ProductMapper.productModelToProductDto(productModel, new ProductDto())).toList());

    }
}
