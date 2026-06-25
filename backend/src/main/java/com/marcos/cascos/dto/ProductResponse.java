package com.marcos.cascos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private Long id;
    private Long productModelId;
    private String productModelName;
    private Long supplierId;
    private String supplierName;
    private String name;
    private String description;
    private String productUrl;
    private String serialNumber;
    private String color;
    private BigDecimal usualPurchasePrice;
    private BigDecimal purchaseShippingCost;
    private BigDecimal otherPurchaseCosts;
    private BigDecimal totalPurchaseCost;
    private BigDecimal recommendedSalePrice;
    private Integer currentStock;
    private Integer reservedStock;
    private Integer minimumStock;
    private Boolean active;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductModelId() { return productModelId; }
    public void setProductModelId(Long productModelId) { this.productModelId = productModelId; }
    public String getProductModelName() { return productModelName; }
    public void setProductModelName(String productModelName) { this.productModelName = productModelName; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProductUrl() { return productUrl; }
    public void setProductUrl(String productUrl) { this.productUrl = productUrl; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public BigDecimal getUsualPurchasePrice() { return usualPurchasePrice; }
    public void setUsualPurchasePrice(BigDecimal usualPurchasePrice) { this.usualPurchasePrice = usualPurchasePrice; }
    public BigDecimal getPurchaseShippingCost() { return purchaseShippingCost; }
    public void setPurchaseShippingCost(BigDecimal purchaseShippingCost) { this.purchaseShippingCost = purchaseShippingCost; }
    public BigDecimal getOtherPurchaseCosts() { return otherPurchaseCosts; }
    public void setOtherPurchaseCosts(BigDecimal otherPurchaseCosts) { this.otherPurchaseCosts = otherPurchaseCosts; }
    public BigDecimal getTotalPurchaseCost() { return totalPurchaseCost; }
    public void setTotalPurchaseCost(BigDecimal totalPurchaseCost) { this.totalPurchaseCost = totalPurchaseCost; }
    public BigDecimal getRecommendedSalePrice() { return recommendedSalePrice; }
    public void setRecommendedSalePrice(BigDecimal recommendedSalePrice) { this.recommendedSalePrice = recommendedSalePrice; }
    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }
    public Integer getReservedStock() { return reservedStock; }
    public void setReservedStock(Integer reservedStock) { this.reservedStock = reservedStock; }
    public Integer getMinimumStock() { return minimumStock; }
    public void setMinimumStock(Integer minimumStock) { this.minimumStock = minimumStock; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
