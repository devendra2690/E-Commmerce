package com.online.buy.order.processor.client;

import com.online.buy.common.code.dto.inventory.InventoryClientDto;

public interface InventoryClient {
    InventoryClientDto validateInventories(InventoryClientDto inventoryClientDto);
}
