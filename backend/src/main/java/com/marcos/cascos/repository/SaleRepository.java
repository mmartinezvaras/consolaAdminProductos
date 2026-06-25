package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Sale;
import com.marcos.cascos.enums.SaleStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Override
    @EntityGraph(attributePaths = {"items", "items.product", "items.product.productModel"})
    List<Sale> findAll();
    List<Sale> findByStatus(SaleStatus status);
    List<Sale> findBySaleDateBetween(LocalDateTime from, LocalDateTime to);
}
