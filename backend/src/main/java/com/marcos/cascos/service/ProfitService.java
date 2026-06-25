package com.marcos.cascos.service;

import com.marcos.cascos.dto.ProfitSummaryResponse;
import java.time.LocalDateTime;

public interface ProfitService {
    ProfitSummaryResponse getSummary(LocalDateTime dateFrom, LocalDateTime dateTo);
    ProfitSummaryResponse getByPeriod(LocalDateTime dateFrom, LocalDateTime dateTo);
    ProfitSummaryResponse getByProduct(Long productId);
    ProfitSummaryResponse getByModel(Long modelId);
    ProfitSummaryResponse getBySale(Long saleId);
}
