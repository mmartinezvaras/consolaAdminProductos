package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.SaleItemResponse;
import com.marcos.cascos.entity.SaleItem;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class SaleItemMapper {
    public SaleItemResponse toResponse(SaleItem item) {
        BigDecimal subtotal = item.getUnitSalePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        BigDecimal cost = item.getUnitPurchaseCost() == null ? BigDecimal.ZERO : item.getUnitPurchaseCost();
        BigDecimal profit = subtotal.subtract(cost.multiply(BigDecimal.valueOf(item.getQuantity())));
        SaleItemResponse response = new SaleItemResponse();
        response.setId(item.getId());
        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
        }
        response.setQuantity(item.getQuantity());
        response.setUnitSalePrice(item.getUnitSalePrice());
        response.setUnitPurchaseCost(item.getUnitPurchaseCost());
        response.setSubtotal(subtotal);
        response.setProfit(profit);
        return response;
    }
}
