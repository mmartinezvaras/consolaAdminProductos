package com.marcos.cascos.dto;

import java.math.BigDecimal;

public class ProfitSummaryResponse {
    private BigDecimal income;
    private BigDecimal productCost;
    private BigDecimal saleCosts;
    private BigDecimal generalExpenses;
    private BigDecimal grossProfit;
    private BigDecimal netProfit;

    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public BigDecimal getProductCost() { return productCost; }
    public void setProductCost(BigDecimal productCost) { this.productCost = productCost; }
    public BigDecimal getSaleCosts() { return saleCosts; }
    public void setSaleCosts(BigDecimal saleCosts) { this.saleCosts = saleCosts; }
    public BigDecimal getGeneralExpenses() { return generalExpenses; }
    public void setGeneralExpenses(BigDecimal generalExpenses) { this.generalExpenses = generalExpenses; }
    public BigDecimal getGrossProfit() { return grossProfit; }
    public void setGrossProfit(BigDecimal grossProfit) { this.grossProfit = grossProfit; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
}
