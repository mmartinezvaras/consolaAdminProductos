package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.InventorySummaryResponse;
import com.marcos.cascos.entity.Product;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class InventorySummaryMapper {
    public InventorySummaryResponse toResponse(Product product, BigDecimal averagePurchasePrice) {
        int currentStock = product.getCurrentStock() == null ? 0 : product.getCurrentStock();
        int reservedStock = product.getReservedStock() == null ? 0 : product.getReservedStock();
        int minimumStock = product.getMinimumStock() == null ? 0 : product.getMinimumStock();
        int availableStock = Math.max(0, currentStock - reservedStock);

        InventorySummaryResponse response = new InventorySummaryResponse();
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setModelName(product.getProductModel() != null ? product.getProductModel().getName() : null);
        response.setCurrentStock(currentStock);
        response.setReservedStock(reservedStock);
        response.setAvailableStock(availableStock);
        response.setMinimumStock(minimumStock);
        response.setAveragePurchasePrice(averagePurchasePrice);
        response.setInventoryValue(averagePurchasePrice.multiply(BigDecimal.valueOf(currentStock)));
        response.setLowStock(currentStock <= minimumStock);
        response.setActive(product.getActive());
        return response;
    }
}
