package com.online.buy.product.processor.mapper;

import com.online.buy.product.processor.entity.Product;
import com.online.buy.product.processor.model.ProductModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class ProductMapperConfig {

    public static void configure(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<ProductModel, Product>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Skip id
                skip(destination.getClient()); // Skip client
                skip(destination.getCreatedAt()); // Skip client
            }
        });
    }
}
