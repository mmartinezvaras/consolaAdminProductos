package com.marcos.cascos.repository;

import com.marcos.cascos.entity.InventoryMovement;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    @Override
    @EntityGraph(attributePaths = {"product", "product.productModel", "purchase"})
    List<InventoryMovement> findAll();

    @EntityGraph(attributePaths = {"product", "product.productModel", "purchase"})
    List<InventoryMovement> findByProductIdOrderByMovementDateDesc(Long productId);

    @EntityGraph(attributePaths = {"product", "product.productModel", "purchase"})
    List<InventoryMovement> findByPurchaseIdOrderByMovementDateDesc(Long purchaseId);
}
