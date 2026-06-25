package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.InventoryMovementResponse;
import com.marcos.cascos.entity.InventoryMovement;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementMapper {
    public InventoryMovementResponse toResponse(InventoryMovement movement) {
        InventoryMovementResponse response = new InventoryMovementResponse();
        response.setId(movement.getId());
        if (movement.getProduct() != null) {
            response.setProductId(movement.getProduct().getId());
            response.setProductName(movement.getProduct().getName());
        }
        if (movement.getPurchase() != null) {
            response.setPurchaseId(movement.getPurchase().getId());
        }
        response.setMovementType(movement.getMovementType());
        response.setQuantity(movement.getQuantity());
        response.setPreviousStock(movement.getPreviousStock());
        response.setResultingStock(movement.getResultingStock());
        response.setReason(movement.getReason());
        response.setNotes(movement.getNotes());
        response.setMovementDate(movement.getMovementDate());
        response.setCreatedAt(movement.getCreatedAt());
        return response;
    }
}
