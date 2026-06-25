package com.marcos.cascos.service;

import com.marcos.cascos.dto.ChartDataResponse;
import com.marcos.cascos.dto.DashboardSummaryResponse;
import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.SaleResponse;
import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse summary();
    List<SaleResponse> recentSales();
    List<ExpenseResponse> recentExpenses();
    List<PurchaseResponse> pendingPurchases();
    List<ProductResponse> lowStock();
    List<ChartDataResponse> salesByMonth();
    List<ChartDataResponse> profitByMonth();
    List<ChartDataResponse> expensesByCategory();
    List<ChartDataResponse> topProducts();
    List<ChartDataResponse> financialEvolution();
    List<ChartDataResponse> stockByModel();
}
