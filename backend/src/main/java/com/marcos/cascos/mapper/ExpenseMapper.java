package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.ExpenseResponse;
import com.marcos.cascos.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setSupplierId(expense.getSupplier() != null ? expense.getSupplier().getId() : null);
        response.setPurchaseId(expense.getPurchase() != null ? expense.getPurchase().getId() : null);
        response.setSaleId(expense.getSale() != null ? expense.getSale().getId() : null);
        response.setConcept(expense.getConcept());
        response.setDescription(expense.getDescription());
        response.setCategory(expense.getCategory());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setNotes(expense.getNotes());
        return response;
    }
}
