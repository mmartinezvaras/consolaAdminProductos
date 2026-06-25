package com.marcos.cascos.controller;

import com.marcos.cascos.dto.ProfitSummaryResponse;
import com.marcos.cascos.service.ProfitService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profits")
public class ProfitController {
    private final ProfitService profitService;

    public ProfitController(ProfitService profitService) {
        this.profitService = profitService;
    }

    @GetMapping("/summary")
    public ProfitSummaryResponse summary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return profitService.getSummary(dateFrom, dateTo);
    }

    @GetMapping("/by-period")
    public ProfitSummaryResponse byPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return profitService.getByPeriod(dateFrom, dateTo);
    }

    @GetMapping("/by-product/{productId}")
    public ProfitSummaryResponse byProduct(@PathVariable Long productId) {
        return profitService.getByProduct(productId);
    }

    @GetMapping("/by-model/{modelId}")
    public ProfitSummaryResponse byModel(@PathVariable Long modelId) {
        return profitService.getByModel(modelId);
    }

    @GetMapping("/by-sale/{saleId}")
    public ProfitSummaryResponse bySale(@PathVariable Long saleId) {
        return profitService.getBySale(saleId);
    }
}
