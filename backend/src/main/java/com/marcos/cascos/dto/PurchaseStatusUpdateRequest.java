package com.marcos.cascos.dto;

import com.marcos.cascos.enums.PurchaseStatus;
import jakarta.validation.constraints.NotNull;

public class PurchaseStatusUpdateRequest {
    @NotNull
    private PurchaseStatus status;

    public PurchaseStatus getStatus() { return status; }
    public void setStatus(PurchaseStatus status) { this.status = status; }
}
