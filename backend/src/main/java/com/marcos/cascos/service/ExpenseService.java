package com.marcos.cascos.service;

import com.marcos.cascos.dto.ExpenseCreateRequest;
import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.dto.ExpenseUpdateRequest;
import com.marcos.cascos.enums.ExpenseCategory;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {
    List<ExpenseResponse> findAll(ExpenseCategory category, LocalDateTime dateFrom, LocalDateTime dateTo);
    ExpenseResponse findById(Long id);
    ExpenseResponse create(ExpenseCreateRequest request);
    ExpenseResponse update(Long id, ExpenseUpdateRequest request);
    void delete(Long id);
}
