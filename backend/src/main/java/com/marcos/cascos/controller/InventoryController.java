package com.marcos.cascos.controller;

import com.marcos.cascos.dto.InventoryAdjustmentRequest;
import com.marcos.cascos.dto.InventoryMovementResponse;
import com.marcos.cascos.dto.InventorySummaryResponse;
import com.marcos.cascos.service.InventoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventorySummaryResponse> getInventory() {
        return inventoryService.getInventory();
    }

    @GetMapping("/movements")
    public List<InventoryMovementResponse> getMovements() {
        return inventoryService.getMovements();
    }

    @GetMapping("/movements/product/{productId}")
    public List<InventoryMovementResponse> getMovementsByProduct(@PathVariable Long productId) {
        return inventoryService.getMovementsByProduct(productId);
    }

    @PostMapping("/adjustments")
    public ResponseEntity<InventoryMovementResponse> adjustStock(@Valid @RequestBody InventoryAdjustmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.adjustStock(request));
    }
}
