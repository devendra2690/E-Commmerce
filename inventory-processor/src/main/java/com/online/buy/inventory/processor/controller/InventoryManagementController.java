package com.online.buy.inventory.processor.controller;

import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.inventory.processor.mapper.InventoryMapper;
import com.online.buy.inventory.processor.model.OrderModel;
import com.online.buy.inventory.processor.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory-management")
@AllArgsConstructor
public class InventoryManagementController {

    private final InventoryService inventoryService;

    @PostMapping("/validate-inventory")
    public ResponseEntity<InventoryClientDto> validateProduct(@RequestBody @Valid InventoryClientDto inventoryClientDto) {

        OrderModel orderModel = InventoryMapper.dtoToModel(inventoryClientDto, new OrderModel());
        inventoryService.updateInventoryIfAvailable(orderModel);
        return ResponseEntity.ok(InventoryMapper.modelToDto(orderModel, inventoryClientDto));
    }

}
