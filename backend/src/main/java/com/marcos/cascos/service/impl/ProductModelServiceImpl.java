package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ProductModelCreateRequest;
import com.marcos.cascos.dto.ProductModelResponse;
import com.marcos.cascos.dto.ProductModelUpdateRequest;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.exception.ConflictException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ProductModelMapper;
import com.marcos.cascos.repository.ProductModelRepository;
import com.marcos.cascos.service.ProductModelService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductModelServiceImpl implements ProductModelService {
    private final ProductModelRepository repository;
    private final ProductModelMapper mapper;

    public ProductModelServiceImpl(ProductModelRepository repository, ProductModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductModelResponse> findAll() {
        return repository.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductModelResponse> findActive() {
        return repository.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductModelResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public ProductModelResponse create(ProductModelCreateRequest request) {
        if (repository.existsByNameIgnoreCaseAndActiveTrue(request.getName())) {
            throw new ConflictException("Product model name already exists");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public ProductModelResponse update(Long id, ProductModelUpdateRequest request) {
        ProductModel model = getEntity(id);
        if (repository.existsByNameIgnoreCaseAndActiveTrueAndIdNot(request.getName(), id)) {
            throw new ConflictException("Product model name already exists");
        }
        mapper.updateEntity(model, request);
        return mapper.toResponse(repository.save(model));
    }

    @Override
    public void delete(Long id) {
        ProductModel model = getEntity(id);
        model.setActive(false);
        repository.save(model);
    }

    private ProductModel getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product model not found: " + id));
    }
}
