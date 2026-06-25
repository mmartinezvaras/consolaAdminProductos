package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Purchase;
import com.marcos.cascos.enums.PurchaseStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Override
    @EntityGraph(attributePaths = {"supplier", "items", "items.product", "items.product.productModel"})
    List<Purchase> findAll();

    @EntityGraph(attributePaths = {"supplier", "items", "items.product", "items.product.productModel"})
    List<Purchase> findByStatus(PurchaseStatus status);

    @EntityGraph(attributePaths = {"supplier", "items", "items.product", "items.product.productModel"})
    List<Purchase> findBySupplierId(Long supplierId);

    @EntityGraph(attributePaths = {"supplier", "items", "items.product", "items.product.productModel"})
    List<Purchase> findByOrderDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Purchase> findByStatusIn(List<PurchaseStatus> statuses);
    boolean existsByIdAndStockAppliedTrue(Long id);
}
