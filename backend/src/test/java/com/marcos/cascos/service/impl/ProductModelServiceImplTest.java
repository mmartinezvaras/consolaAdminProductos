package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.ProductModelCreateRequest;
import com.marcos.cascos.dto.ProductModelUpdateRequest;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ProductModelMapper;
import com.marcos.cascos.repository.ProductModelRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductModelServiceImplTest {
    @Mock
    private ProductModelRepository repository;

    private ProductModelServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductModelServiceImpl(repository, new ProductModelMapper());
    }

    @Test
    void createsProductModel() {
        ProductModelCreateRequest request = new ProductModelCreateRequest();
        request.setName("AirPods Pro");
        ProductModel model = new ProductModel();
        model.setId(1L);
        model.setName("AirPods Pro");

        when(repository.existsByNameIgnoreCaseAndActiveTrue("AirPods Pro")).thenReturn(false);
        when(repository.save(any(ProductModel.class))).thenAnswer(invocation -> {
            ProductModel saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        assertEquals("AirPods Pro", service.create(request).getName());
    }

    @Test
    void getsProductModelById() {
        ProductModel model = new ProductModel();
        model.setId(1L);
        model.setName("Sony XM5");

        when(repository.findById(1L)).thenReturn(Optional.of(model));

        assertEquals("Sony XM5", service.findById(1L).getName());
    }

    @Test
    void updatesProductModel() {
        ProductModel model = new ProductModel();
        model.setId(1L);
        model.setName("Old");
        ProductModelUpdateRequest request = new ProductModelUpdateRequest();
        request.setName("New");

        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(repository.existsByNameIgnoreCaseAndActiveTrueAndIdNot("New", 1L)).thenReturn(false);
        when(repository.save(model)).thenReturn(model);
        service.update(1L, request);
        assertEquals("New", model.getName());
    }

    @Test
    void throwsWhenProductModelMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
}
