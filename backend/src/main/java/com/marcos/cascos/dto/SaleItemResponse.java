package com.marcos.cascos.dto;

import java.math.BigDecimal;

public class SaleItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitSalePrice;
    private BigDecimal unitPurchaseCost;
    private BigDecimal subtotal;
    private BigDecimal profit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitSalePrice() { return unitSalePrice; }
    public void setUnitSalePrice(BigDecimal unitSalePrice) { this.unitSalePrice = unitSalePrice; }
    public BigDecimal getUnitPurchaseCost() { return unitPurchaseCost; }
    public void setUnitPurchaseCost(BigDecimal unitPurchaseCost) { this.unitPurchaseCost = unitPurchaseCost; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit; }
}
