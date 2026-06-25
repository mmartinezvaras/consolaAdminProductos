package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.SupplierCreateRequest;
import com.marcos.cascos.dto.SupplierResponse;
import com.marcos.cascos.dto.SupplierUpdateRequest;
import com.marcos.cascos.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public Supplier toEntity(SupplierCreateRequest request) {
        Supplier supplier = new Supplier();
        updateEntity(supplier, request);
        supplier.setActive(request.getActive() != null ? request.getActive() : true);
        return supplier;
    }

    public void updateEntity(Supplier supplier, SupplierUpdateRequest request) {
        updateEntity(supplier, (SupplierCreateRequest) request);
    }

    private void updateEntity(Supplier supplier, SupplierCreateRequest request) {
        supplier.setName(request.getName());
        supplier.setWebsite(request.getWebsite());
        supplier.setPurchaseUrl(request.getPurchaseUrl());
        supplier.setContactName(request.getContactName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setNotes(request.getNotes());
        supplier.setActive(request.getActive() != null ? request.getActive() : supplier.getActive());
    }

    public SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setWebsite(supplier.getWebsite());
        response.setPurchaseUrl(supplier.getPurchaseUrl());
        response.setContactName(supplier.getContactName());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setNotes(supplier.getNotes());
        response.setActive(supplier.getActive());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        return response;
    }
}
