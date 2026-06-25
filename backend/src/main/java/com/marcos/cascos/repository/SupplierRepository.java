package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Supplier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);
    boolean existsByNameIgnoreCaseAndActiveTrueAndIdNot(String name, Long id);
}
