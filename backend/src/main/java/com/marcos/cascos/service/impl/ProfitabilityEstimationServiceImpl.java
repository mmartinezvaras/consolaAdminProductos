package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ProfitabilityEstimationRequest;
import com.marcos.cascos.dto.ProfitabilityEstimationResponse;
import com.marcos.cascos.entity.ProfitabilityEstimation;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ProfitabilityEstimationMapper;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.ProfitabilityEstimationRepository;
import com.marcos.cascos.service.ProfitabilityEstimationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfitabilityEstimationServiceImpl implements ProfitabilityEstimationService {
    private final ProfitabilityEstimationRepository repository;
    private final ProductRepository productRepository;
    private final ProfitabilityEstimationMapper mapper;

    public ProfitabilityEstimationServiceImpl(ProfitabilityEstimationRepository repository, ProductRepository productRepository, ProfitabilityEstimationMapper mapper) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfitabilityEstimationResponse calculate(ProfitabilityEstimationRequest request) {
        return mapper.toResponse(build(request));
    }

    @Override
    public ProfitabilityEstimationResponse create(ProfitabilityEstimationRequest request) {
        return mapper.toResponse(repository.save(build(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfitabilityEstimationResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfitabilityEstimationResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Estimation not found: " + id)));
    }

    @Override
    public void delete(Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Estimation not found: " + id)));
    }

    private ProfitabilityEstimation build(ProfitabilityEstimationRequest request) {
        int units = request.getUnits();
        BigDecimal unitPurchase = value(request.getUnitPurchasePrice());
        BigDecimal purchaseShipping = value(request.getPurchaseShippingCost());
        BigDecimal otherPurchase = value(request.getOtherPurchaseCosts());
        BigDecimal unitSale = value(request.getEstimatedUnitSalePrice());
        BigDecimal fixedCommission = value(request.getFixedCommission());
        BigDecimal percentageCommission = value(request.getPercentageCommission());
        BigDecimal buyerShipping = value(request.getBuyerShippingCost());
        BigDecimal otherCosts = value(request.getOtherCosts());
        BigDecimal lossPct = value(request.getEstimatedLossPercentage());

        BigDecimal grossIncome = unitSale.multiply(BigDecimal.valueOf(units));
        BigDecimal expectedLoss = grossIncome.multiply(lossPct).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal estimatedIncome = grossIncome.subtract(expectedLoss);
        BigDecimal totalInvestment = unitPurchase.multiply(BigDecimal.valueOf(units)).add(purchaseShipping).add(otherPurchase);
        BigDecimal percentageCost = estimatedIncome.multiply(percentageCommission).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal estimatedExpenses = fixedCommission.add(percentageCost).add(buyerShipping).add(otherCosts);
        BigDecimal profit = estimatedIncome.subtract(totalInvestment).subtract(estimatedExpenses);
        BigDecimal profitPerUnit = profit.divide(BigDecimal.valueOf(units), 2, RoundingMode.HALF_UP);
        BigDecimal margin = estimatedIncome.signum() == 0 ? BigDecimal.ZERO : profit.multiply(new BigDecimal("100")).divide(estimatedIncome, 2, RoundingMode.HALF_UP);
        BigDecimal roi = totalInvestment.signum() == 0 ? BigDecimal.ZERO : profit.multiply(new BigDecimal("100")).divide(totalInvestment, 2, RoundingMode.HALF_UP);
        BigDecimal breakEvenPrice = totalInvestment.add(estimatedExpenses).divide(BigDecimal.valueOf(units), 2, RoundingMode.HALF_UP);
        int breakEvenUnits = unitSale.signum() == 0 ? 0 : totalInvestment.add(estimatedExpenses).divide(unitSale, 0, RoundingMode.CEILING).intValue();

        ProfitabilityEstimation estimation = new ProfitabilityEstimation();
        estimation.setName(request.getName());
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.getProductId()));
            estimation.setProduct(product);
        }
        estimation.setUnits(units);
        estimation.setUnitPurchasePrice(unitPurchase);
        estimation.setPurchaseShippingCost(purchaseShipping);
        estimation.setOtherPurchaseCosts(otherPurchase);
        estimation.setEstimatedUnitSalePrice(unitSale);
        estimation.setFixedCommission(fixedCommission);
        estimation.setPercentageCommission(percentageCommission);
        estimation.setBuyerShippingCost(buyerShipping);
        estimation.setOtherCosts(otherCosts);
        estimation.setEstimatedLossPercentage(lossPct);
        estimation.setTotalInvestment(totalInvestment);
        estimation.setEstimatedIncome(estimatedIncome);
        estimation.setEstimatedExpenses(estimatedExpenses);
        estimation.setEstimatedProfit(profit);
        estimation.setProfitPerUnit(profitPerUnit);
        estimation.setMarginPercentage(margin);
        estimation.setRoiPercentage(roi);
        estimation.setBreakEvenPrice(breakEvenPrice);
        estimation.setBreakEvenUnits(breakEvenUnits);
        return estimation;
    }

    private BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
