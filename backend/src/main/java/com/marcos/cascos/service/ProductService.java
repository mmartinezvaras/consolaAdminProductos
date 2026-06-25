package com.marcos.cascos.service;

import com.marcos.cascos.dto.ProductCreateRequest;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.ProductUpdateRequest;
import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
    List<ProductResponse> findActive();
    List<ProductResponse> findLowStock();
    ProductResponse findById(Long id);
    ProductResponse create(ProductCreateRequest request);
    ProductResponse update(Long id, ProductUpdateRequest request);
    void delete(Long id);
}
