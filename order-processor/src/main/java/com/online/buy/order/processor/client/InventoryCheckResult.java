package com.online.buy.order.processor.client;

import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class InventoryCheckResult {
    private final InventoryClientDto inventoryClientDto;
    private final Map<Long, Map<String, Object>> errorMap;
}
