package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.SupplierCreateRequest;
import com.marcos.cascos.dto.SupplierResponse;
import com.marcos.cascos.dto.SupplierUpdateRequest;
import com.marcos.cascos.entity.Supplier;
import com.marcos.cascos.exception.ConflictException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.SupplierMapper;
import com.marcos.cascos.repository.SupplierRepository;
import com.marcos.cascos.service.SupplierService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repository;
    private final SupplierMapper mapper;

    public SupplierServiceImpl(SupplierRepository repository, SupplierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> findAll() {
        return repository.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public SupplierResponse create(SupplierCreateRequest request) {
        if (repository.existsByNameIgnoreCaseAndActiveTrue(request.getName())) {
            throw new ConflictException("Supplier name already exists");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public SupplierResponse update(Long id, SupplierUpdateRequest request) {
        Supplier supplier = getEntity(id);
        if (repository.existsByNameIgnoreCaseAndActiveTrueAndIdNot(request.getName(), id)) {
            throw new ConflictException("Supplier name already exists");
        }
        mapper.updateEntity(supplier, request);
        return mapper.toResponse(repository.save(supplier));
    }

    @Override
    public void delete(Long id) {
        Supplier supplier = getEntity(id);
        supplier.setActive(false);
        repository.save(supplier);
    }

    private Supplier getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }
}
