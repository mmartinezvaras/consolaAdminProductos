package com.marcos.cascos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductCreateRequest {
    private Long productModelId;

    @Size(max = 255)
    private String productModelName;

    private Long supplierId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Size(max = 500)
    private String productUrl;

    @Size(max = 100)
    private String serialNumber;

    @Size(max = 100)
    private String color;

    @DecimalMin("0.00")
    private BigDecimal usualPurchasePrice;

    @DecimalMin("0.00")
    private BigDecimal purchaseShippingCost;

    @DecimalMin("0.00")
    private BigDecimal otherPurchaseCosts;

    @DecimalMin("0.00")
    private BigDecimal recommendedSalePrice;

    @Min(0)
    private Integer currentStock;

    @Min(0)
    private Integer reservedStock;

    @Min(0)
    private Integer minimumStock;

    private Boolean active;

    @Size(max = 2000)
    private String notes;

    public Long getProductModelId() { return productModelId; }
    public void setProductModelId(Long productModelId) { this.productModelId = productModelId; }
    public String getProductModelName() { return productModelName; }
    public void setProductModelName(String productModelName) { this.productModelName = productModelName; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
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
}
