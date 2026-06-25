package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.PurchaseItemResponse;
import com.marcos.cascos.entity.PurchaseItem;
import org.springframework.stereotype.Component;

@Component
public class PurchaseItemMapper {
    public PurchaseItemResponse toResponse(PurchaseItem item) {
        PurchaseItemResponse response = new PurchaseItemResponse();
        response.setId(item.getId());
        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
        }
        response.setQuantity(item.getQuantity());
        response.setUnitPurchasePrice(item.getUnitPurchasePrice());
        return response;
    }
}
