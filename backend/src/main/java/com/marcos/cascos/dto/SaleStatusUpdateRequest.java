package com.marcos.cascos.dto;

import com.marcos.cascos.enums.SaleStatus;
import jakarta.validation.constraints.NotNull;

public class SaleStatusUpdateRequest {
    @NotNull
    private SaleStatus status;

    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
}
