package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.PurchaseCreateRequest;
import com.marcos.cascos.dto.PurchaseItemRequest;
import com.marcos.cascos.dto.PurchaseStatusUpdateRequest;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.Purchase;
import com.marcos.cascos.entity.Supplier;
import com.marcos.cascos.enums.PurchaseStatus;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.PurchaseItemMapper;
import com.marcos.cascos.mapper.PurchaseMapper;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.PurchaseRepository;
import com.marcos.cascos.repository.SupplierRepository;
import com.marcos.cascos.service.InventoryService;
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
class PurchaseServiceImplTest {
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InventoryService inventoryService;

    private PurchaseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PurchaseServiceImpl(
                purchaseRepository,
                supplierRepository,
                productRepository,
                inventoryService,
                new PurchaseMapper(new PurchaseItemMapper()));
    }

    @Test
    void createsPurchaseWithMultipleItems() {
        PurchaseCreateRequest request = validRequest(PurchaseStatus.PENDING);
        request.setItems(List.of(itemRequest(1L, 2), itemRequest(2L, 3)));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product(1L, 5)));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product(2L, 7)));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase purchase = invocation.getArgument(0);
            purchase.setId(10L);
            return purchase;
        });

        assertEquals(2, service.create(request).getItems().size());
    }

    @Test
    void rejectsPurchaseWithoutItems() {
        PurchaseCreateRequest request = validRequest(PurchaseStatus.PENDING);
        request.setItems(List.of());

        assertThrows(BusinessException.class, () -> service.create(request));
    }

    @Test
    void rejectsQuantityZeroOrNegativeAndNegativePrice() {
        PurchaseCreateRequest zero = validRequest(PurchaseStatus.PENDING);
        zero.setItems(List.of(itemRequest(1L, 0)));
        assertThrows(BusinessException.class, () -> service.create(zero));

        PurchaseCreateRequest negative = validRequest(PurchaseStatus.PENDING);
        negative.setItems(List.of(itemRequest(1L, -1)));
        assertThrows(BusinessException.class, () -> service.create(negative));

        PurchaseCreateRequest negativePrice = validRequest(PurchaseStatus.PENDING);
        PurchaseItemRequest item = itemRequest(1L, 1);
        item.setUnitPurchasePrice(new BigDecimal("-1.00"));
        negativePrice.setItems(List.of(item));
        assertThrows(BusinessException.class, () -> service.create(negativePrice));
    }

    @Test
    void rejectsMissingSupplierOrProduct() {
        PurchaseCreateRequest request = validRequest(PurchaseStatus.PENDING);
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.create(request));

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier()));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
    }

    @Test
    void appliesStockWhenCreatedAsReceived() {
        PurchaseCreateRequest request = validRequest(PurchaseStatus.RECEIVED);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product(1L, 5)));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doAnswer(invocation -> {
            Purchase purchase = invocation.getArgument(0);
            purchase.setStockApplied(true);
            return null;
        }).when(inventoryService).applyPurchaseStock(any(Purchase.class));

        assertEquals(true, service.create(request).getStockApplied());
        verify(inventoryService).applyPurchaseStock(any(Purchase.class));
    }

    @Test
    void updatesStatusToReceivedAndCancelled() {
        Purchase purchase = purchase();
        when(purchaseRepository.findById(10L)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        doAnswer(invocation -> {
            Purchase p = invocation.getArgument(0);
            p.setStockApplied(true);
            return null;
        }).when(inventoryService).applyPurchaseStock(purchase);

        PurchaseStatusUpdateRequest received = new PurchaseStatusUpdateRequest();
        received.setStatus(PurchaseStatus.RECEIVED);
        assertEquals(PurchaseStatus.RECEIVED, service.updateStatus(10L, received).getStatus());

        doAnswer(invocation -> {
            Purchase p = invocation.getArgument(0);
            p.setStockApplied(false);
            return null;
        }).when(inventoryService).revertPurchaseStock(purchase);
        PurchaseStatusUpdateRequest cancelled = new PurchaseStatusUpdateRequest();
        cancelled.setStatus(PurchaseStatus.CANCELLED);
        assertEquals(PurchaseStatus.CANCELLED, service.updateStatus(10L, cancelled).getStatus());
    }

    private PurchaseCreateRequest validRequest(PurchaseStatus status) {
        PurchaseCreateRequest request = new PurchaseCreateRequest();
        request.setSupplierId(1L);
        request.setOrderDate(LocalDateTime.now());
        request.setStatus(status);
        request.setShippingCost(BigDecimal.ZERO);
        request.setOtherCosts(BigDecimal.ZERO);
        request.setItems(List.of(itemRequest(1L, 2)));
        return request;
    }

    private PurchaseItemRequest itemRequest(Long productId, int quantity) {
        PurchaseItemRequest item = new PurchaseItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUnitPurchasePrice(new BigDecimal("10.00"));
        return item;
    }

    private Supplier supplier() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Supplier");
        return supplier;
    }

    private Product product(Long id, int stock) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setCurrentStock(stock);
        product.setReservedStock(0);
        product.setMinimumStock(1);
        product.setUsualPurchasePrice(new BigDecimal("10.00"));
        return product;
    }

    private Purchase purchase() {
        Purchase purchase = new Purchase();
        purchase.setId(10L);
        purchase.setSupplier(supplier());
        purchase.setOrderDate(LocalDateTime.now());
        purchase.setStatus(PurchaseStatus.PENDING);
        purchase.setShippingCost(BigDecimal.ZERO);
        purchase.setOtherCosts(BigDecimal.ZERO);
        purchase.setStockApplied(false);
        return purchase;
    }
}
