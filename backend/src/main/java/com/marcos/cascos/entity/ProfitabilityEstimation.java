package com.marcos.cascos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "profitability_estimations")
public class ProfitabilityEstimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private Integer units;
    @Column(name = "unit_purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPurchasePrice;
    @Column(name = "purchase_shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchaseShippingCost;
    @Column(name = "other_purchase_costs", nullable = false, precision = 10, scale = 2)
    private BigDecimal otherPurchaseCosts;
    @Column(name = "estimated_unit_sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedUnitSalePrice;
    @Column(name = "fixed_commission", nullable = false, precision = 10, scale = 2)
    private BigDecimal fixedCommission;
    @Column(name = "percentage_commission", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentageCommission;
    @Column(name = "buyer_shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal buyerShippingCost;
    @Column(name = "other_costs", nullable = false, precision = 10, scale = 2)
    private BigDecimal otherCosts;
    @Column(name = "estimated_loss_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal estimatedLossPercentage;
    @Column(name = "total_investment", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalInvestment;
    @Column(name = "estimated_income", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedIncome;
    @Column(name = "estimated_expenses", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedExpenses;
    @Column(name = "estimated_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedProfit;
    @Column(name = "profit_per_unit", precision = 10, scale = 2)
    private BigDecimal profitPerUnit;
    @Column(name = "margin_percentage", precision = 5, scale = 2)
    private BigDecimal marginPercentage;
    @Column(name = "roi_percentage", precision = 5, scale = 2)
    private BigDecimal roiPercentage;
    @Column(name = "break_even_price", precision = 10, scale = 2)
    private BigDecimal breakEvenPrice;
    @Column(name = "break_even_units")
    private Integer breakEvenUnits;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
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
    public BigDecimal getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(BigDecimal totalInvestment) { this.totalInvestment = totalInvestment; }
    public BigDecimal getEstimatedIncome() { return estimatedIncome; }
    public void setEstimatedIncome(BigDecimal estimatedIncome) { this.estimatedIncome = estimatedIncome; }
    public BigDecimal getEstimatedExpenses() { return estimatedExpenses; }
    public void setEstimatedExpenses(BigDecimal estimatedExpenses) { this.estimatedExpenses = estimatedExpenses; }
    public BigDecimal getEstimatedProfit() { return estimatedProfit; }
    public void setEstimatedProfit(BigDecimal estimatedProfit) { this.estimatedProfit = estimatedProfit; }
    public BigDecimal getProfitPerUnit() { return profitPerUnit; }
    public void setProfitPerUnit(BigDecimal profitPerUnit) { this.profitPerUnit = profitPerUnit; }
    public BigDecimal getMarginPercentage() { return marginPercentage; }
    public void setMarginPercentage(BigDecimal marginPercentage) { this.marginPercentage = marginPercentage; }
    public BigDecimal getRoiPercentage() { return roiPercentage; }
    public void setRoiPercentage(BigDecimal roiPercentage) { this.roiPercentage = roiPercentage; }
    public BigDecimal getBreakEvenPrice() { return breakEvenPrice; }
    public void setBreakEvenPrice(BigDecimal breakEvenPrice) { this.breakEvenPrice = breakEvenPrice; }
    public Integer getBreakEvenUnits() { return breakEvenUnits; }
    public void setBreakEvenUnits(Integer breakEvenUnits) { this.breakEvenUnits = breakEvenUnits; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
