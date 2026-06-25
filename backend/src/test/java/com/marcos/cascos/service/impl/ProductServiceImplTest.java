package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.ProductCreateRequest;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ConflictException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ProductMapper;
import com.marcos.cascos.repository.ProductModelRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.SupplierRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductModelRepository productModelRepository;

    @Mock
    private SupplierRepository supplierRepository;

    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(productRepository, productModelRepository, supplierRepository, new ProductMapper());
    }

    @Test
    void createsProduct() {
        ProductCreateRequest request = validRequest();
        ProductModel model = new ProductModel();
        model.setId(1L);
        model.setName("Model");

        when(productRepository.existsBySerialNumberIgnoreCase("SN-1")).thenReturn(false);
        when(productModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        assertEquals("AirPods", service.create(request).getName());
    }

    @Test
    void rejectsNegativePrice() {
        ProductCreateRequest request = validRequest();
        request.setUsualPurchasePrice(new BigDecimal("-1.00"));

        assertThrows(BusinessException.class, () -> service.create(request));
    }

    @Test
    void rejectsReservedStockGreaterThanCurrentStock() {
        ProductCreateRequest request = validRequest();
        request.setCurrentStock(1);
        request.setReservedStock(2);

        assertThrows(BusinessException.class, () -> service.create(request));
    }

    @Test
    void throwsWhenProductMissing() {
        when(productRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(404L));
    }

    @Test
    void deleteDeactivatesProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        assertFalse(product.getActive());
    }

    @Test
    void rejectsDuplicateSerialNumber() {
        ProductCreateRequest request = validRequest();
        when(productRepository.existsBySerialNumberIgnoreCase("SN-1")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(request));
    }

    private ProductCreateRequest validRequest() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setProductModelId(1L);
        request.setName("AirPods");
        request.setSerialNumber("SN-1");
        request.setColor("White");
        request.setUsualPurchasePrice(new BigDecimal("10.00"));
        request.setRecommendedSalePrice(new BigDecimal("20.00"));
        request.setCurrentStock(3);
        request.setReservedStock(1);
        request.setMinimumStock(1);
        return request;
    }
}
