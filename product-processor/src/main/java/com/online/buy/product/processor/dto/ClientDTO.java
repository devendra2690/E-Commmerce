package com.online.buy.product.processor.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private List<ProductDto> products;
}
