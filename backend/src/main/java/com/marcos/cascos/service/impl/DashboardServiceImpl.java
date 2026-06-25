package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ChartDataResponse;
import com.marcos.cascos.dto.DashboardSummaryResponse;
import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.entity.Expense;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.Sale;
import com.marcos.cascos.entity.SaleItem;
import com.marcos.cascos.enums.PurchaseStatus;
import com.marcos.cascos.enums.SaleStatus;
import com.marcos.cascos.mapper.ExpenseMapper;
import com.marcos.cascos.mapper.ProductMapper;
import com.marcos.cascos.mapper.PurchaseMapper;
import com.marcos.cascos.mapper.SaleMapper;
import com.marcos.cascos.repository.ExpenseRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.PurchaseRepository;
import com.marcos.cascos.repository.SaleRepository;
import com.marcos.cascos.service.DashboardService;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ExpenseRepository expenseRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductMapper productMapper;
    private final SaleMapper saleMapper;
    private final ExpenseMapper expenseMapper;
    private final PurchaseMapper purchaseMapper;

    public DashboardServiceImpl(ProductRepository productRepository, SaleRepository saleRepository, ExpenseRepository expenseRepository, PurchaseRepository purchaseRepository, ProductMapper productMapper, SaleMapper saleMapper, ExpenseMapper expenseMapper, PurchaseMapper purchaseMapper) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.expenseRepository = expenseRepository;
        this.purchaseRepository = purchaseRepository;
        this.productMapper = productMapper;
        this.saleMapper = saleMapper;
        this.expenseMapper = expenseMapper;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public DashboardSummaryResponse summary() {
        List<Product> products = productRepository.findAll();
        BigDecimal inventoryValue = products.stream()
                .map(p -> value(p.getUsualPurchasePrice()).multiply(BigDecimal.valueOf(stock(p))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal income = completedSales().stream().flatMap(s -> s.getItems().stream()).map(this::income).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal productCost = completedSales().stream().flatMap(s -> s.getItems().stream()).map(this::cost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saleCosts = completedSales().stream().map(s -> value(s.getCommission()).add(value(s.getShippingCost())).add(value(s.getOtherCosts()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal generalExpenses = expenseRepository.findBySaleIsNullAndPurchaseIsNull().stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalStockUnits(products.stream().mapToInt(this::stock).sum());
        response.setInventoryValue(inventoryValue);
        response.setTotalIncome(income);
        response.setTotalExpenses(generalExpenses.add(saleCosts));
        response.setGrossProfit(income.subtract(productCost).subtract(saleCosts));
        response.setNetProfit(response.getGrossProfit().subtract(generalExpenses));
        response.setTotalSales((long) completedSales().size());
        response.setPendingPurchases((long) purchaseRepository.findByStatusIn(List.of(PurchaseStatus.PENDING, PurchaseStatus.PAID, PurchaseStatus.SHIPPED)).size());
        response.setLowStockProducts(productRepository.findLowStockProducts().stream().count());
        return response;
    }

    @Override
    public List<SaleResponse> recentSales() {
        return saleRepository.findAll().stream().sorted(Comparator.comparing(Sale::getSaleDate).reversed()).limit(10).map(saleMapper::toResponse).toList();
    }

    @Override
    public List<ExpenseResponse> recentExpenses() {
        return expenseRepository.findAll().stream().sorted(Comparator.comparing(Expense::getExpenseDate).reversed()).limit(10).map(expenseMapper::toResponse).toList();
    }

    @Override
    public List<PurchaseResponse> pendingPurchases() {
        return purchaseRepository.findByStatusIn(List.of(PurchaseStatus.PENDING, PurchaseStatus.PAID, PurchaseStatus.SHIPPED)).stream().map(purchaseMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> lowStock() {
        return productRepository.findLowStockProducts().stream().map(productMapper::toResponse).toList();
    }

    @Override
    public List<ChartDataResponse> salesByMonth() {
        return completedSales().stream().collect(Collectors.groupingBy(s -> s.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.reducing(BigDecimal.ZERO, this::saleIncome, BigDecimal::add)))
                .entrySet().stream().map(e -> new ChartDataResponse(e.getKey(), e.getValue())).toList();
    }

    @Override
    public List<ChartDataResponse> profitByMonth() {
        return completedSales().stream().collect(Collectors.groupingBy(s -> s.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.reducing(BigDecimal.ZERO, this::saleProfit, BigDecimal::add)))
                .entrySet().stream().map(e -> new ChartDataResponse(e.getKey(), e.getValue())).toList();
    }

    @Override
    public List<ChartDataResponse> expensesByCategory() {
        return expenseRepository.findAll().stream().collect(Collectors.groupingBy(e -> e.getCategory() == null ? "OTHER" : e.getCategory().name(), Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)))
                .entrySet().stream().map(e -> new ChartDataResponse(e.getKey(), e.getValue())).toList();
    }

    @Override
    public List<ChartDataResponse> topProducts() {
        Map<String, BigDecimal> values = completedSales().stream().flatMap(s -> s.getItems().stream())
                .collect(Collectors.groupingBy(i -> i.getProduct().getName(), Collectors.reducing(BigDecimal.ZERO, i -> BigDecimal.valueOf(i.getQuantity()), BigDecimal::add)));
        return values.entrySet().stream().sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed()).limit(10).map(e -> new ChartDataResponse(e.getKey(), e.getValue())).toList();
    }

    @Override
    public List<ChartDataResponse> financialEvolution() {
        return profitByMonth();
    }

    @Override
    public List<ChartDataResponse> stockByModel() {
        return productRepository.findAll().stream().collect(Collectors.groupingBy(p -> p.getProductModel() == null ? "Sin modelo" : p.getProductModel().getName(), Collectors.reducing(BigDecimal.ZERO, p -> BigDecimal.valueOf(stock(p)), BigDecimal::add)))
                .entrySet().stream().map(e -> new ChartDataResponse(e.getKey(), e.getValue())).toList();
    }

    private List<Sale> completedSales() {
        return saleRepository.findAll().stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED).toList();
    }

    private BigDecimal saleIncome(Sale sale) {
        return sale.getItems().stream().map(this::income).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal saleProfit(Sale sale) {
        return saleIncome(sale)
                .subtract(sale.getItems().stream().map(this::cost).reduce(BigDecimal.ZERO, BigDecimal::add))
                .subtract(value(sale.getCommission()))
                .subtract(value(sale.getShippingCost()))
                .subtract(value(sale.getOtherCosts()));
    }

    private BigDecimal income(SaleItem item) { return value(item.getUnitSalePrice()).multiply(BigDecimal.valueOf(item.getQuantity())); }
    private BigDecimal cost(SaleItem item) { return value(item.getUnitPurchaseCost()).multiply(BigDecimal.valueOf(item.getQuantity())); }
    private BigDecimal value(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private int stock(Product product) { return product.getCurrentStock() == null ? 0 : product.getCurrentStock(); }
}
