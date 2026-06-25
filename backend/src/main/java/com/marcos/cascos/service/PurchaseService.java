package com.marcos.cascos.service;

import com.marcos.cascos.dto.PurchaseCreateRequest;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.PurchaseStatusUpdateRequest;
import com.marcos.cascos.dto.PurchaseUpdateRequest;
import com.marcos.cascos.enums.PurchaseStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {
    List<PurchaseResponse> findAll(PurchaseStatus status, Long supplierId, LocalDateTime dateFrom, LocalDateTime dateTo);
    PurchaseResponse findById(Long id);
    PurchaseResponse create(PurchaseCreateRequest request);
    PurchaseResponse update(Long id, PurchaseUpdateRequest request);
    PurchaseResponse updateStatus(Long id, PurchaseStatusUpdateRequest request);
    void delete(Long id);
}
