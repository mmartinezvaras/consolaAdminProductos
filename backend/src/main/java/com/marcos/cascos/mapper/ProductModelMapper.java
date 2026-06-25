package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.ProductModelCreateRequest;
import com.marcos.cascos.dto.ProductModelResponse;
import com.marcos.cascos.dto.ProductModelUpdateRequest;
import com.marcos.cascos.entity.ProductModel;
import org.springframework.stereotype.Component;

@Component
public class ProductModelMapper {
    public ProductModel toEntity(ProductModelCreateRequest request) {
        ProductModel model = new ProductModel();
        updateEntity(model, request);
        model.setActive(request.getActive() != null ? request.getActive() : true);
        return model;
    }

    public void updateEntity(ProductModel model, ProductModelUpdateRequest request) {
        updateEntity(model, (ProductModelCreateRequest) request);
    }

    private void updateEntity(ProductModel model, ProductModelCreateRequest request) {
        model.setName(request.getName());
        model.setBrand(request.getBrand());
        model.setCategory(request.getCategory());
        model.setGeneration(request.getGeneration());
        model.setDescription(request.getDescription());
        model.setActive(request.getActive() != null ? request.getActive() : model.getActive());
    }

    public ProductModelResponse toResponse(ProductModel model) {
        ProductModelResponse response = new ProductModelResponse();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setBrand(model.getBrand());
        response.setCategory(model.getCategory());
        response.setGeneration(model.getGeneration());
        response.setDescription(model.getDescription());
        response.setActive(model.getActive());
        response.setCreatedAt(model.getCreatedAt());
        response.setUpdatedAt(model.getUpdatedAt());
        return response;
    }
}
