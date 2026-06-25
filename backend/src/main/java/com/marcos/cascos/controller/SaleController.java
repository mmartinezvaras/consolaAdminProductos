package com.marcos.cascos.controller;

import com.marcos.cascos.dto.SaleCreateRequest;
import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.dto.SaleStatusUpdateRequest;
import com.marcos.cascos.dto.SaleUpdateRequest;
import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import com.marcos.cascos.service.SaleService;
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
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleResponse> findAll(
            @RequestParam(required = false) SaleStatus status,
            @RequestParam(required = false) SalePlatform platform,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return saleService.findAll(status, platform, paymentMethod, dateFrom, dateTo);
    }

    @GetMapping("/{id}")
    public SaleResponse findById(@PathVariable Long id) {
        return saleService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SaleResponse> create(@Valid @RequestBody SaleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(request));
    }

    @PutMapping("/{id}")
    public SaleResponse update(@PathVariable Long id, @Valid @RequestBody SaleUpdateRequest request) {
        return saleService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public SaleResponse updateStatus(@PathVariable Long id, @Valid @RequestBody SaleStatusUpdateRequest request) {
        return saleService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
