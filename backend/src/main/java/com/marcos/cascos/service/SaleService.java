package com.marcos.cascos.service;

import com.marcos.cascos.dto.SaleCreateRequest;
import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.dto.SaleStatusUpdateRequest;
import com.marcos.cascos.dto.SaleUpdateRequest;
import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    List<SaleResponse> findAll(SaleStatus status, SalePlatform platform, PaymentMethod paymentMethod, LocalDateTime dateFrom, LocalDateTime dateTo);
    SaleResponse findById(Long id);
    SaleResponse create(SaleCreateRequest request);
    SaleResponse update(Long id, SaleUpdateRequest request);
    SaleResponse updateStatus(Long id, SaleStatusUpdateRequest request);
    void delete(Long id);
}
