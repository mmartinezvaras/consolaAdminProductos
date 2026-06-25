package com.marcos.cascos.controller;

import com.marcos.cascos.dto.PurchaseCreateRequest;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.PurchaseStatusUpdateRequest;
import com.marcos.cascos.dto.PurchaseUpdateRequest;
import com.marcos.cascos.enums.PurchaseStatus;
import com.marcos.cascos.service.PurchaseService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<PurchaseResponse> findAll(
            @RequestParam(required = false) PurchaseStatus status,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return purchaseService.findAll(status, supplierId, dateFrom, dateTo);
    }

    @GetMapping("/{id}")
    public PurchaseResponse findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody PurchaseCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.create(request));
    }

    @PutMapping("/{id}")
    public PurchaseResponse update(@PathVariable Long id, @Valid @RequestBody PurchaseUpdateRequest request) {
        return purchaseService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public PurchaseResponse updateStatus(@PathVariable Long id, @Valid @RequestBody PurchaseStatusUpdateRequest request) {
        return purchaseService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
