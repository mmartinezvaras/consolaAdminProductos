package com.marcos.cascos.repository;

import com.marcos.cascos.entity.ProductModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByActiveTrue();
    Optional<ProductModel> findByNameIgnoreCaseAndActiveTrue(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);
    boolean existsByNameIgnoreCaseAndActiveTrueAndIdNot(String name, Long id);
}
