package com.marcos.cascos.service;

import com.marcos.cascos.dto.SupplierCreateRequest;
import com.marcos.cascos.dto.SupplierResponse;
import com.marcos.cascos.dto.SupplierUpdateRequest;
import java.util.List;

public interface SupplierService {
    List<SupplierResponse> findAll();
    SupplierResponse findById(Long id);
    SupplierResponse create(SupplierCreateRequest request);
    SupplierResponse update(Long id, SupplierUpdateRequest request);
    void delete(Long id);
}
