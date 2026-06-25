package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.ProductCreateRequest;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.ProductUpdateRequest;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.entity.Supplier;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductCreateRequest request, ProductModel model, Supplier supplier) {
        Product product = new Product();
        updateEntity(product, request, model, supplier);
        product.setActive(request.getActive() != null ? request.getActive() : true);
        return product;
    }

    public void updateEntity(Product product, ProductUpdateRequest request, ProductModel model, Supplier supplier) {
        updateEntity(product, (ProductCreateRequest) request, model, supplier);
    }

    private void updateEntity(Product product, ProductCreateRequest request, ProductModel model, Supplier supplier) {
        product.setProductModel(model);
        product.setSupplier(supplier);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setProductUrl(normalizeBlank(request.getProductUrl()));
        product.setSerialNumber(normalizeBlank(request.getSerialNumber()));
        product.setColor(defaultText(request.getColor(), "Sin color"));
        BigDecimal purchasePrice = request.getUsualPurchasePrice() != null ? request.getUsualPurchasePrice() : BigDecimal.ZERO;
        product.setUsualPurchasePrice(purchasePrice);
        product.setRecommendedSalePrice(request.getRecommendedSalePrice() != null ? request.getRecommendedSalePrice() : purchasePrice);
        product.setCurrentStock(request.getCurrentStock() != null ? request.getCurrentStock() : 0);
        product.setReservedStock(request.getReservedStock() != null ? request.getReservedStock() : 0);
        product.setMinimumStock(request.getMinimumStock() != null ? request.getMinimumStock() : 0);
        product.setActive(request.getActive() != null ? request.getActive() : product.getActive());
        product.setNotes(request.getNotes());
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        if (product.getProductModel() != null) {
            response.setProductModelId(product.getProductModel().getId());
            response.setProductModelName(product.getProductModel().getName());
        }
        if (product.getSupplier() != null) {
            response.setSupplierId(product.getSupplier().getId());
            response.setSupplierName(product.getSupplier().getName());
        }
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setProductUrl(product.getProductUrl());
        response.setSerialNumber(product.getSerialNumber());
        response.setColor(product.getColor());
        response.setUsualPurchasePrice(product.getUsualPurchasePrice());
        response.setRecommendedSalePrice(product.getRecommendedSalePrice());
        response.setCurrentStock(product.getCurrentStock());
        response.setReservedStock(product.getReservedStock());
        response.setMinimumStock(product.getMinimumStock());
        response.setActive(product.getActive());
        response.setNotes(product.getNotes());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

    private String normalizeBlank(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
