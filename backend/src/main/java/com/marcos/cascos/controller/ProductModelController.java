package com.marcos.cascos.controller;

import com.marcos.cascos.dto.ProductModelCreateRequest;
import com.marcos.cascos.dto.ProductModelResponse;
import com.marcos.cascos.dto.ProductModelUpdateRequest;
import com.marcos.cascos.service.ProductModelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-models")
public class ProductModelController {
    private final ProductModelService service;

    public ProductModelController(ProductModelService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductModelResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/active")
    public List<ProductModelResponse> findActive() {
        return service.findActive();
    }

    @GetMapping("/{id}")
    public ProductModelResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProductModelResponse> create(@Valid @RequestBody ProductModelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ProductModelResponse update(@PathVariable Long id, @Valid @RequestBody ProductModelUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
