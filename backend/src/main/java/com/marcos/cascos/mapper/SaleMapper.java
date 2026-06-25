package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.entity.Sale;
import com.marcos.cascos.entity.SaleItem;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {
    private final SaleItemMapper itemMapper;

    public SaleMapper(SaleItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public SaleResponse toResponse(Sale sale) {
        BigDecimal income = sale.getItems().stream().map(this::income).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal productCost = sale.getItems().stream().map(this::cost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saleCosts = value(sale.getCommission()).add(value(sale.getShippingCost())).add(value(sale.getOtherCosts()));
        SaleResponse response = new SaleResponse();
        response.setId(sale.getId());
        response.setBuyerReference(sale.getBuyerReference());
        response.setSaleDate(sale.getSaleDate());
        response.setPaymentMethod(sale.getPaymentMethod());
        response.setPlatform(sale.getPlatform());
        response.setStatus(sale.getStatus());
        response.setCommission(sale.getCommission());
        response.setShippingCost(sale.getShippingCost());
        response.setOtherCosts(sale.getOtherCosts());
        response.setStockApplied(sale.getStockApplied());
        response.setNotes(sale.getNotes());
        response.setItems(sale.getItems().stream().map(itemMapper::toResponse).toList());
        response.setIncome(income);
        response.setProductCost(productCost);
        response.setProfit(income.subtract(productCost).subtract(saleCosts));
        response.setCreatedAt(sale.getCreatedAt());
        response.setUpdatedAt(sale.getUpdatedAt());
        return response;
    }

    private BigDecimal income(SaleItem item) {
        return value(item.getUnitSalePrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private BigDecimal cost(SaleItem item) {
        return value(item.getUnitPurchaseCost()).multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
