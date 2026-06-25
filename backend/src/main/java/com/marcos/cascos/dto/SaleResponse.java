package com.marcos.cascos.dto;

import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponse {
    private Long id;
    private String buyerReference;
    private LocalDateTime saleDate;
    private PaymentMethod paymentMethod;
    private SalePlatform platform;
    private SaleStatus status;
    private BigDecimal commission;
    private BigDecimal shippingCost;
    private BigDecimal otherCosts;
    private Boolean stockApplied;
    private String notes;
    private List<SaleItemResponse> items;
    private BigDecimal income;
    private BigDecimal productCost;
    private BigDecimal profit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBuyerReference() { return buyerReference; }
    public void setBuyerReference(String buyerReference) { this.buyerReference = buyerReference; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public SalePlatform getPlatform() { return platform; }
    public void setPlatform(SalePlatform platform) { this.platform = platform; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }
    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }
    public BigDecimal getOtherCosts() { return otherCosts; }
    public void setOtherCosts(BigDecimal otherCosts) { this.otherCosts = otherCosts; }
    public Boolean getStockApplied() { return stockApplied; }
    public void setStockApplied(Boolean stockApplied) { this.stockApplied = stockApplied; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<SaleItemResponse> getItems() { return items; }
    public void setItems(List<SaleItemResponse> items) { this.items = items; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public BigDecimal getProductCost() { return productCost; }
    public void setProductCost(BigDecimal productCost) { this.productCost = productCost; }
    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
