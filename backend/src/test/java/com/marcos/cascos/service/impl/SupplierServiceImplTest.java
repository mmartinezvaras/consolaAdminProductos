package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.SupplierCreateRequest;
import com.marcos.cascos.entity.Supplier;
import com.marcos.cascos.mapper.SupplierMapper;
import com.marcos.cascos.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {
    @Mock
    private SupplierRepository repository;

    private SupplierServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SupplierServiceImpl(repository, new SupplierMapper());
    }

    @Test
    void createsSupplier() {
        SupplierCreateRequest request = new SupplierCreateRequest();
        request.setName("Proveedor");
        request.setEmail("proveedor@example.com");

        when(repository.existsByNameIgnoreCaseAndActiveTrue("Proveedor")).thenReturn(false);
        when(repository.save(org.mockito.ArgumentMatchers.any(Supplier.class))).thenAnswer(invocation -> {
            Supplier saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        assertEquals("proveedor@example.com", service.create(request).getEmail());
    }
}
