package com.marcos.cascos.dto;

import java.math.BigDecimal;

public class ProfitabilityEstimationResponse {
    private Long id;
    private String name;
    private Long productId;
    private Integer units;
    private BigDecimal totalInvestment;
    private BigDecimal estimatedIncome;
    private BigDecimal estimatedExpenses;
    private BigDecimal estimatedProfit;
    private BigDecimal profitPerUnit;
    private BigDecimal marginPercentage;
    private BigDecimal roiPercentage;
    private BigDecimal breakEvenPrice;
    private Integer breakEvenUnits;
    private String profitabilityStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getUnits() { return units; }
    public void setUnits(Integer units) { this.units = units; }
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
    public String getProfitabilityStatus() { return profitabilityStatus; }
    public void setProfitabilityStatus(String profitabilityStatus) { this.profitabilityStatus = profitabilityStatus; }
}
