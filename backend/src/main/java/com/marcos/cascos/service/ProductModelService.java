package com.marcos.cascos.service;

import com.marcos.cascos.dto.ProductModelCreateRequest;
import com.marcos.cascos.dto.ProductModelResponse;
import com.marcos.cascos.dto.ProductModelUpdateRequest;
import java.util.List;

public interface ProductModelService {
    List<ProductModelResponse> findAll();
    List<ProductModelResponse> findActive();
    ProductModelResponse findById(Long id);
    ProductModelResponse create(ProductModelCreateRequest request);
    ProductModelResponse update(Long id, ProductModelUpdateRequest request);
    void delete(Long id);
}
