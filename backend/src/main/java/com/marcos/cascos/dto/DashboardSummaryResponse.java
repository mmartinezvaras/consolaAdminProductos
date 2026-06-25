package com.marcos.cascos.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponse {
    private Integer totalStockUnits;
    private BigDecimal inventoryValue;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal grossProfit;
    private BigDecimal netProfit;
    private Long totalSales;
    private Long pendingPurchases;
    private Long lowStockProducts;

    public Integer getTotalStockUnits() { return totalStockUnits; }
    public void setTotalStockUnits(Integer totalStockUnits) { this.totalStockUnits = totalStockUnits; }
    public BigDecimal getInventoryValue() { return inventoryValue; }
    public void setInventoryValue(BigDecimal inventoryValue) { this.inventoryValue = inventoryValue; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getGrossProfit() { return grossProfit; }
    public void setGrossProfit(BigDecimal grossProfit) { this.grossProfit = grossProfit; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
    public Long getTotalSales() { return totalSales; }
    public void setTotalSales(Long totalSales) { this.totalSales = totalSales; }
    public Long getPendingPurchases() { return pendingPurchases; }
    public void setPendingPurchases(Long pendingPurchases) { this.pendingPurchases = pendingPurchases; }
    public Long getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(Long lowStockProducts) { this.lowStockProducts = lowStockProducts; }
}
