package com.online.buy.order.processor.client;

import com.online.buy.order.processor.client.dto.InventoryRequest;

import java.util.List;
import java.util.Map;

public interface InventoryClient {
    Map<Long,String> validateInventories(List<InventoryRequest> inventoryRequest);
}
