package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ProfitSummaryResponse;
import com.marcos.cascos.entity.Expense;
import com.marcos.cascos.entity.Sale;
import com.marcos.cascos.entity.SaleItem;
import com.marcos.cascos.enums.SaleStatus;
import com.marcos.cascos.repository.ExpenseRepository;
import com.marcos.cascos.repository.SaleRepository;
import com.marcos.cascos.service.ProfitService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfitServiceImpl implements ProfitService {
    private final SaleRepository saleRepository;
    private final ExpenseRepository expenseRepository;

    public ProfitServiceImpl(SaleRepository saleRepository, ExpenseRepository expenseRepository) {
        this.saleRepository = saleRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ProfitSummaryResponse getSummary(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return calculate(dateFrom, dateTo, null, null, null);
    }

    @Override
    public ProfitSummaryResponse getByPeriod(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return getSummary(dateFrom, dateTo);
    }

    @Override
    public ProfitSummaryResponse getByProduct(Long productId) {
        return calculate(null, null, productId, null, null);
    }

    @Override
    public ProfitSummaryResponse getByModel(Long modelId) {
        return calculate(null, null, null, modelId, null);
    }

    @Override
    public ProfitSummaryResponse getBySale(Long saleId) {
        return calculate(null, null, null, null, saleId);
    }

    private ProfitSummaryResponse calculate(LocalDateTime from, LocalDateTime to, Long productId, Long modelId, Long saleId) {
        var sales = saleRepository.findAll().stream()
                .filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .filter(s -> saleId == null || s.getId().equals(saleId))
                .filter(s -> from == null || !s.getSaleDate().isBefore(from))
                .filter(s -> to == null || !s.getSaleDate().isAfter(to))
                .toList();
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal productCost = BigDecimal.ZERO;
        BigDecimal saleCosts = BigDecimal.ZERO;
        for (Sale sale : sales) {
            BigDecimal saleIncome = BigDecimal.ZERO;
            BigDecimal saleProductCost = BigDecimal.ZERO;
            for (SaleItem item : sale.getItems()) {
                if (productId != null && !item.getProduct().getId().equals(productId)) {
                    continue;
                }
                if (modelId != null && !item.getProduct().getProductModel().getId().equals(modelId)) {
                    continue;
                }
                saleIncome = saleIncome.add(item.getUnitSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                saleProductCost = saleProductCost.add(value(item.getUnitPurchaseCost()).multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            if (saleIncome.signum() > 0 || (productId == null && modelId == null)) {
                income = income.add(saleIncome);
                productCost = productCost.add(saleProductCost);
                saleCosts = saleCosts.add(value(sale.getCommission())).add(value(sale.getShippingCost())).add(value(sale.getOtherCosts()));
            }
        }
        BigDecimal generalExpenses = expenseRepository.findAll().stream()
                .filter(e -> e.getSale() == null && e.getPurchase() == null)
                .filter(e -> from == null || !e.getExpenseDate().isBefore(from))
                .filter(e -> to == null || !e.getExpenseDate().isAfter(to))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ProfitSummaryResponse response = new ProfitSummaryResponse();
        response.setIncome(income);
        response.setProductCost(productCost);
        response.setSaleCosts(saleCosts);
        response.setGeneralExpenses(generalExpenses);
        response.setGrossProfit(income.subtract(productCost).subtract(saleCosts));
        response.setNetProfit(response.getGrossProfit().subtract(generalExpenses));
        return response;
    }

    private BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
