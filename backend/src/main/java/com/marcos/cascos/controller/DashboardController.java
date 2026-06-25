package com.marcos.cascos.controller;

import com.marcos.cascos.dto.ChartDataResponse;
import com.marcos.cascos.dto.DashboardSummaryResponse;
import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.service.DashboardService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service = service; }

    @GetMapping("/summary")
    public DashboardSummaryResponse summary() { return service.summary(); }
    @GetMapping("/recent-sales")
    public List<SaleResponse> recentSales() { return service.recentSales(); }
    @GetMapping("/recent-expenses")
    public List<ExpenseResponse> recentExpenses() { return service.recentExpenses(); }
    @GetMapping("/pending-purchases")
    public List<PurchaseResponse> pendingPurchases() { return service.pendingPurchases(); }
    @GetMapping("/low-stock")
    public List<ProductResponse> lowStock() { return service.lowStock(); }
    @GetMapping("/charts/sales-by-month")
    public List<ChartDataResponse> salesByMonth() { return service.salesByMonth(); }
    @GetMapping("/charts/profit-by-month")
    public List<ChartDataResponse> profitByMonth() { return service.profitByMonth(); }
    @GetMapping("/charts/expenses-by-category")
    public List<ChartDataResponse> expensesByCategory() { return service.expensesByCategory(); }
    @GetMapping("/charts/top-products")
    public List<ChartDataResponse> topProducts() { return service.topProducts(); }
    @GetMapping("/charts/financial-evolution")
    public List<ChartDataResponse> financialEvolution() { return service.financialEvolution(); }
    @GetMapping("/charts/stock-by-model")
    public List<ChartDataResponse> stockByModel() { return service.stockByModel(); }
}
