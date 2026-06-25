package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Expense;
import com.marcos.cascos.enums.ExpenseCategory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(ExpenseCategory category);
    List<Expense> findByExpenseDateBetween(LocalDateTime from, LocalDateTime to);
    List<Expense> findBySaleIsNullAndPurchaseIsNull();
}
