package com.marcos.cascos.service;

import com.marcos.cascos.dto.ProfitabilityEstimationRequest;
import com.marcos.cascos.dto.ProfitabilityEstimationResponse;
import java.util.List;

public interface ProfitabilityEstimationService {
    ProfitabilityEstimationResponse calculate(ProfitabilityEstimationRequest request);
    ProfitabilityEstimationResponse create(ProfitabilityEstimationRequest request);
    List<ProfitabilityEstimationResponse> findAll();
    ProfitabilityEstimationResponse findById(Long id);
    void delete(Long id);
}
