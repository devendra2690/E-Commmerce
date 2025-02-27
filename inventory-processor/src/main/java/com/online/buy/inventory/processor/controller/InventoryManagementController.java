package com.online.buy.inventory.processor.controller;

import com.online.buy.inventory.processor.dto.OrderItemDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory-management")
public class InventoryManagementController {

    @PostMapping("/validate-inventory")
    public Map<Long, String> validateProduct(@RequestBody @Valid List<OrderItemDto> items) {

        return null;
    }

}
