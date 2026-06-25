package com.marcos.cascos.dto;

import java.math.BigDecimal;

public class ChartDataResponse {
    private String label;
    private BigDecimal value;

    public ChartDataResponse(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public BigDecimal getValue() { return value; }
}
