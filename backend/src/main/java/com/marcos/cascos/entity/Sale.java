package com.marcos.cascos.entity;

import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "buyer_reference", length = 255)
    private String buyerReference;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 50)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SalePlatform platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SaleStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commission;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "other_costs", nullable = false, precision = 10, scale = 2)
    private BigDecimal otherCosts;

    @Column(name = "stock_applied", nullable = false)
    private Boolean stockApplied;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.saleDate == null) {
            this.saleDate = now;
        }
        if (this.status == null) {
            this.status = SaleStatus.PENDING;
        }
        if (this.commission == null) {
            this.commission = BigDecimal.ZERO;
        }
        if (this.shippingCost == null) {
            this.shippingCost = BigDecimal.ZERO;
        }
        if (this.otherCosts == null) {
            this.otherCosts = BigDecimal.ZERO;
        }
        if (this.stockApplied == null) {
            this.stockApplied = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(SaleItem item) {
        items.add(item);
        item.setSale(this);
    }

    public void clearItems() {
        items.forEach(item -> item.setSale(null));
        items.clear();
    }

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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }
}
