package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ExpenseCreateRequest;
import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.dto.ExpenseUpdateRequest;
import com.marcos.cascos.entity.Expense;
import com.marcos.cascos.enums.ExpenseCategory;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ExpenseMapper;
import com.marcos.cascos.repository.ExpenseRepository;
import com.marcos.cascos.repository.PurchaseRepository;
import com.marcos.cascos.repository.SaleRepository;
import com.marcos.cascos.repository.SupplierRepository;
import com.marcos.cascos.service.ExpenseService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
    private final ExpenseMapper mapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, SupplierRepository supplierRepository, PurchaseRepository purchaseRepository, SaleRepository saleRepository, ExpenseMapper mapper) {
        this.expenseRepository = expenseRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseRepository = purchaseRepository;
        this.saleRepository = saleRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> findAll(ExpenseCategory category, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return expenseRepository.findAll().stream()
                .filter(e -> category == null || e.getCategory() == category)
                .filter(e -> dateFrom == null || !e.getExpenseDate().isBefore(dateFrom))
                .filter(e -> dateTo == null || !e.getExpenseDate().isAfter(dateTo))
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse findById(Long id) {
        return mapper.toResponse(getExpense(id));
    }

    @Override
    public ExpenseResponse create(ExpenseCreateRequest request) {
        Expense expense = new Expense();
        fill(expense, request);
        return mapper.toResponse(expenseRepository.save(expense));
    }

    @Override
    public ExpenseResponse update(Long id, ExpenseUpdateRequest request) {
        Expense expense = getExpense(id);
        fill(expense, request);
        return mapper.toResponse(expenseRepository.save(expense));
    }

    @Override
    public void delete(Long id) {
        expenseRepository.delete(getExpense(id));
    }

    private void fill(Expense expense, ExpenseCreateRequest request) {
        if (request.getAmount() != null && request.getAmount().signum() < 0) {
            throw new BusinessException("Expense amount cannot be negative");
        }
        expense.setSupplier(request.getSupplierId() == null ? null : supplierRepository.findById(request.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + request.getSupplierId())));
        expense.setPurchase(request.getPurchaseId() == null ? null : purchaseRepository.findById(request.getPurchaseId()).orElseThrow(() -> new ResourceNotFoundException("Purchase not found: " + request.getPurchaseId())));
        expense.setSale(request.getSaleId() == null ? null : saleRepository.findById(request.getSaleId()).orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + request.getSaleId())));
        expense.setConcept(request.getConcept());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setNotes(request.getNotes());
    }

    private Expense getExpense(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
    }
}
