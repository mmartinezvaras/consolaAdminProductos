package com.marcos.cascos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProfitabilityEstimationRequest {
    @NotBlank
    private String name;
    private Long productId;
    @NotNull @Min(1)
    private Integer units;
    @NotNull @DecimalMin("0.00")
    private BigDecimal unitPurchasePrice;
    @DecimalMin("0.00")
    private BigDecimal purchaseShippingCost;
    @DecimalMin("0.00")
    private BigDecimal otherPurchaseCosts;
    @NotNull @DecimalMin("0.00")
    private BigDecimal estimatedUnitSalePrice;
    @DecimalMin("0.00")
    private BigDecimal fixedCommission;
    @DecimalMin("0.00")
    private BigDecimal percentageCommission;
    @DecimalMin("0.00")
    private BigDecimal buyerShippingCost;
    @DecimalMin("0.00")
    private BigDecimal otherCosts;
    @DecimalMin("0.00")
    private BigDecimal estimatedLossPercentage;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getUnits() { return units; }
    public void setUnits(Integer units) { this.units = units; }
    public BigDecimal getUnitPurchasePrice() { return unitPurchasePrice; }
    public void setUnitPurchasePrice(BigDecimal unitPurchasePrice) { this.unitPurchasePrice = unitPurchasePrice; }
    public BigDecimal getPurchaseShippingCost() { return purchaseShippingCost; }
    public void setPurchaseShippingCost(BigDecimal purchaseShippingCost) { this.purchaseShippingCost = purchaseShippingCost; }
    public BigDecimal getOtherPurchaseCosts() { return otherPurchaseCosts; }
    public void setOtherPurchaseCosts(BigDecimal otherPurchaseCosts) { this.otherPurchaseCosts = otherPurchaseCosts; }
    public BigDecimal getEstimatedUnitSalePrice() { return estimatedUnitSalePrice; }
    public void setEstimatedUnitSalePrice(BigDecimal estimatedUnitSalePrice) { this.estimatedUnitSalePrice = estimatedUnitSalePrice; }
    public BigDecimal getFixedCommission() { return fixedCommission; }
    public void setFixedCommission(BigDecimal fixedCommission) { this.fixedCommission = fixedCommission; }
    public BigDecimal getPercentageCommission() { return percentageCommission; }
    public void setPercentageCommission(BigDecimal percentageCommission) { this.percentageCommission = percentageCommission; }
    public BigDecimal getBuyerShippingCost() { return buyerShippingCost; }
    public void setBuyerShippingCost(BigDecimal buyerShippingCost) { this.buyerShippingCost = buyerShippingCost; }
    public BigDecimal getOtherCosts() { return otherCosts; }
    public void setOtherCosts(BigDecimal otherCosts) { this.otherCosts = otherCosts; }
    public BigDecimal getEstimatedLossPercentage() { return estimatedLossPercentage; }
    public void setEstimatedLossPercentage(BigDecimal estimatedLossPercentage) { this.estimatedLossPercentage = estimatedLossPercentage; }
}
