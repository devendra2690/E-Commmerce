package com.online.buy.registration.processor.mapper;

import com.online.buy.registration.processor.dto.ProductDto;
import com.online.buy.registration.processor.entity.Product;
import com.online.buy.registration.processor.model.ProductModel;

public class ProductMapper {

    public static ProductModel productToProductModel(Product product, ProductModel productModel) {
        productModel.setId(product.getId());
        productModel.setName(product.getName());
        productModel.setPrice(product.getPrice());
        productModel.setCreatedAt(product.getCreatedAt());
        productModel.setUpdatedAt(product.getUpdatedAt());
        productModel.setDescription(product.getDescription());
        productModel.setQuantity(product.getQuantity());
        productModel.setCategory(product.getCategory());

        return productModel;
    }

    public static ProductDto productModelToProductDto(ProductModel productModel, ProductDto productDto) {

        productDto.setId(productModel.getId());
        productDto.setName(productModel.getName());
        productDto.setPrice(productModel.getPrice());
        productDto.setCreatedAt(productModel.getCreatedAt());
        productDto.setUpdatedAt(productModel.getUpdatedAt());
        productDto.setDescription(productModel.getDescription());
        productDto.setQuantity(productModel.getQuantity());
        productDto.setCategory(productModel.getCategory());


        return productDto;
    }

    public static ProductModel productDtoToProductModel(ProductDto productDto, ProductModel productModel) {

        productModel.setName(productDto.getName());
        productModel.setPrice(productDto.getPrice());
        productModel.setDescription(productDto.getDescription());
        productModel.setQuantity(productDto.getQuantity());
        productModel.setCategory(productDto.getCategory());
        return productModel;
    }

    public static Product productModelToProduct(ProductModel productModel,Product product){
        product.setName(productModel.getName());
        product.setPrice(productModel.getPrice());
        product.setDescription(productModel.getDescription());
        product.setQuantity(productModel.getQuantity());
        product.setCategory(productModel.getCategory());
        return product;
    }
}
