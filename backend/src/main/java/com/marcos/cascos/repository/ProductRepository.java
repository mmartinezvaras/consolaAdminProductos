package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("select p from Product p where p.active = true and p.currentStock <= p.minimumStock")
    List<Product> findLowStockProducts();

    boolean existsBySerialNumberIgnoreCase(String serialNumber);
    boolean existsBySerialNumberIgnoreCaseAndIdNot(String serialNumber, Long id);
    Optional<Product> findBySerialNumberIgnoreCase(String serialNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
