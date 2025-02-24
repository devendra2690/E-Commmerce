package com.online.buy.product.processor.controller;

import com.online.buy.product.processor.dto.ProductDto;
import com.online.buy.product.processor.model.ProductModel;
import com.online.buy.product.processor.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> listAllProducts() {
        List<ProductModel> productModels = productService.listAll();
       return ResponseEntity.ok(productModels.stream().map(productModel -> modelMapper.map(productModel, ProductDto.class)).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") @NotNull Long productId) {
       ProductModel productModel = productService.getProductById(productId);
       return ResponseEntity.ok(modelMapper.map(productModel, ProductDto.class));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable("category") @NotNull String category) {
        List<ProductModel> productModels = productService.getProductByCategory(category);
        return ResponseEntity.ok(productModels.stream().map(productModel -> modelMapper.map(productModel, ProductDto.class)).toList());

    }
}
