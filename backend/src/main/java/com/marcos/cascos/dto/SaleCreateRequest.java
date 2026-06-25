package com.marcos.cascos.dto;

import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleCreateRequest {
    @Size(max = 255)
    private String buyerReference;
    @NotNull
    private LocalDateTime saleDate;
    private PaymentMethod paymentMethod;
    private SalePlatform platform;
    private SaleStatus status;
    @DecimalMin("0.00")
    private BigDecimal commission;
    @DecimalMin("0.00")
    private BigDecimal shippingCost;
    @DecimalMin("0.00")
    private BigDecimal otherCosts;
    @Size(max = 2000)
    private String notes;
    @Valid
    @NotEmpty
    private List<SaleItemRequest> items;

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
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }
}
