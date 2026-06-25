package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    private final PurchaseItemMapper itemMapper;

    public PurchaseMapper(PurchaseItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public PurchaseResponse toResponse(Purchase purchase) {
        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        if (purchase.getSupplier() != null) {
            response.setSupplierId(purchase.getSupplier().getId());
            response.setSupplierName(purchase.getSupplier().getName());
        }
        response.setOrderDate(purchase.getOrderDate());
        response.setEstimatedArrivalDate(purchase.getEstimatedArrivalDate());
        response.setActualArrivalDate(purchase.getActualArrivalDate());
        response.setStatus(purchase.getStatus());
        response.setShippingCost(purchase.getShippingCost());
        response.setOtherCosts(purchase.getOtherCosts());
        response.setTrackingNumber(purchase.getTrackingNumber());
        response.setExternalReference(purchase.getExternalReference());
        response.setStockApplied(purchase.getStockApplied());
        response.setNotes(purchase.getNotes());
        response.setItems(purchase.getItems().stream().map(itemMapper::toResponse).toList());
        response.setCreatedAt(purchase.getCreatedAt());
        response.setUpdatedAt(purchase.getUpdatedAt());
        return response;
    }
}
