package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.InventoryAdjustmentRequest;
import com.marcos.cascos.entity.InventoryMovement;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.entity.Purchase;
import com.marcos.cascos.entity.PurchaseItem;
import com.marcos.cascos.enums.InventoryMovementType;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.mapper.InventoryMovementMapper;
import com.marcos.cascos.mapper.InventorySummaryMapper;
import com.marcos.cascos.repository.InventoryMovementRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.PurchaseItemRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PurchaseItemRepository purchaseItemRepository;
    @Mock
    private InventoryMovementRepository movementRepository;

    private InventoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new InventoryServiceImpl(
                productRepository,
                purchaseItemRepository,
                movementRepository,
                new InventoryMovementMapper(),
                new InventorySummaryMapper());
    }

    @Test
    void appliesAndPreventsDoublePurchaseReception() {
        Purchase purchase = purchase(product(1L, 5), 3);
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(purchase.getItems().getFirst().getProduct()));
        when(movementRepository.save(any(InventoryMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.applyPurchaseStock(purchase);

        assertEquals(8, purchase.getItems().getFirst().getProduct().getCurrentStock());
        assertEquals(true, purchase.getStockApplied());
        verify(movementRepository).save(any(InventoryMovement.class));
        assertThrows(BusinessException.class, () -> service.applyPurchaseStock(purchase));
    }

    @Test
    void revertsReceivedPurchaseAndRejectsNegativeStock() {
        Product product = product(1L, 5);
        Purchase purchase = purchase(product, 3);
        purchase.setStockApplied(true);
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(movementRepository.save(any(InventoryMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.revertPurchaseStock(purchase);

        assertEquals(2, product.getCurrentStock());
        assertEquals(false, purchase.getStockApplied());

        product.setCurrentStock(1);
        purchase.setStockApplied(true);
        assertThrows(BusinessException.class, () -> service.revertPurchaseStock(purchase));
    }

    @Test
    void adjustsStockManually() {
        Product product = product(1L, 5);
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(movementRepository.save(any(InventoryMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryAdjustmentRequest positive = adjustment(1L, 4);
        assertEquals(9, service.adjustStock(positive).getResultingStock());

        InventoryAdjustmentRequest negative = adjustment(1L, -2);
        assertEquals(7, service.adjustStock(negative).getResultingStock());
    }

    @Test
    void rejectsInvalidManualAdjustments() {
        Product product = product(1L, 1);
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));

        assertThrows(BusinessException.class, () -> service.adjustStock(adjustment(1L, 0)));
        assertThrows(BusinessException.class, () -> service.adjustStock(adjustment(1L, -2)));
    }

    @Test
    void calculatesInventorySummary() {
        Product product = product(1L, 10);
        product.setReservedStock(2);
        product.setMinimumStock(10);
        PurchaseItem item1 = purchaseItem(product, 2, "10.00");
        PurchaseItem item2 = purchaseItem(product, 2, "20.00");

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(purchaseItemRepository.findReceivedItemsByProductId(1L)).thenReturn(List.of(item1, item2));

        var summary = service.getInventory().getFirst();

        assertEquals(8, summary.getAvailableStock());
        assertEquals(new BigDecimal("15.00"), summary.getAveragePurchasePrice());
        assertEquals(new BigDecimal("150.00"), summary.getInventoryValue());
        assertEquals(true, summary.getLowStock());
    }

    @Test
    void listsMovementsByProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product(1L, 1));
        movement.setMovementType(InventoryMovementType.MANUAL_IN);
        movement.setQuantity(1);
        movement.setPreviousStock(0);
        movement.setResultingStock(1);
        movement.setMovementDate(LocalDateTime.now());
        when(movementRepository.findByProductIdOrderByMovementDateDesc(1L)).thenReturn(List.of(movement));

        assertEquals(1, service.getMovementsByProduct(1L).size());
        verify(movementRepository, times(1)).findByProductIdOrderByMovementDateDesc(1L);
    }

    private InventoryAdjustmentRequest adjustment(Long productId, int quantity) {
        InventoryAdjustmentRequest request = new InventoryAdjustmentRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);
        request.setReason("Manual adjustment");
        return request;
    }

    private Product product(Long id, int stock) {
        ProductModel model = new ProductModel();
        model.setName("Model");
        Product product = new Product();
        product.setId(id);
        product.setProductModel(model);
        product.setName("Product");
        product.setCurrentStock(stock);
        product.setReservedStock(0);
        product.setMinimumStock(1);
        product.setActive(true);
        product.setUsualPurchasePrice(new BigDecimal("9.00"));
        return product;
    }

    private Purchase purchase(Product product, int quantity) {
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setStockApplied(false);
        purchase.setActualArrivalDate(LocalDateTime.now());
        purchase.addItem(purchaseItem(product, quantity, "10.00"));
        return purchase;
    }

    private PurchaseItem purchaseItem(Product product, int quantity, String price) {
        PurchaseItem item = new PurchaseItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPurchasePrice(new BigDecimal(price));
        return item;
    }
}
