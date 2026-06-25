package com.marcos.cascos.repository;

import com.marcos.cascos.entity.PurchaseItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    List<PurchaseItem> findByPurchaseId(Long purchaseId);

    @Query("""
            select pi from PurchaseItem pi
            join fetch pi.purchase p
            where pi.product.id = :productId
              and p.status = com.marcos.cascos.enums.PurchaseStatus.RECEIVED
              and p.stockApplied = true
            """)
    List<PurchaseItem> findReceivedItemsByProductId(@Param("productId") Long productId);
}
