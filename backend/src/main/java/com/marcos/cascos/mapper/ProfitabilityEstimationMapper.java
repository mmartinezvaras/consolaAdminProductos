package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.ProfitabilityEstimationResponse;
import com.marcos.cascos.entity.ProfitabilityEstimation;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ProfitabilityEstimationMapper {
    public ProfitabilityEstimationResponse toResponse(ProfitabilityEstimation estimation) {
        ProfitabilityEstimationResponse response = new ProfitabilityEstimationResponse();
        response.setId(estimation.getId());
        response.setName(estimation.getName());
        response.setProductId(estimation.getProduct() != null ? estimation.getProduct().getId() : null);
        response.setUnits(estimation.getUnits());
        response.setTotalInvestment(estimation.getTotalInvestment());
        response.setEstimatedIncome(estimation.getEstimatedIncome());
        response.setEstimatedExpenses(estimation.getEstimatedExpenses());
        response.setEstimatedProfit(estimation.getEstimatedProfit());
        response.setProfitPerUnit(estimation.getProfitPerUnit());
        response.setMarginPercentage(estimation.getMarginPercentage());
        response.setRoiPercentage(estimation.getRoiPercentage());
        response.setBreakEvenPrice(estimation.getBreakEvenPrice());
        response.setBreakEvenUnits(estimation.getBreakEvenUnits());
        response.setProfitabilityStatus(estimation.getEstimatedProfit().compareTo(BigDecimal.ZERO) >= 0 ? "PROFITABLE" : "LOSS");
        return response;
    }
}
