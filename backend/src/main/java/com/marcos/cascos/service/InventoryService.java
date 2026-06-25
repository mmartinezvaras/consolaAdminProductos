package com.marcos.cascos.service;

import com.marcos.cascos.dto.InventoryAdjustmentRequest;
import com.marcos.cascos.dto.InventoryMovementResponse;
import com.marcos.cascos.dto.InventorySummaryResponse;
import com.marcos.cascos.entity.Purchase;
import java.util.List;

public interface InventoryService {
    List<InventorySummaryResponse> getInventory();
    List<InventoryMovementResponse> getMovements();
    List<InventoryMovementResponse> getMovementsByProduct(Long productId);
    InventoryMovementResponse adjustStock(InventoryAdjustmentRequest request);
    void applyPurchaseStock(Purchase purchase);
    void revertPurchaseStock(Purchase purchase);
}
