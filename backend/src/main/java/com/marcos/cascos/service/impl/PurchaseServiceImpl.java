package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.PurchaseCreateRequest;
import com.marcos.cascos.dto.PurchaseItemRequest;
import com.marcos.cascos.dto.PurchaseResponse;
import com.marcos.cascos.dto.PurchaseStatusUpdateRequest;
import com.marcos.cascos.dto.PurchaseUpdateRequest;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.Purchase;
import com.marcos.cascos.entity.PurchaseItem;
import com.marcos.cascos.entity.Supplier;
import com.marcos.cascos.enums.PurchaseStatus;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.PurchaseMapper;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.PurchaseRepository;
import com.marcos.cascos.repository.SupplierRepository;
import com.marcos.cascos.service.InventoryService;
import com.marcos.cascos.service.PurchaseService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final PurchaseMapper mapper;

    public PurchaseServiceImpl(
            PurchaseRepository purchaseRepository,
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            InventoryService inventoryService,
            PurchaseMapper mapper) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> findAll(PurchaseStatus status, Long supplierId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return purchaseRepository.findAll().stream()
                .filter(purchase -> status == null || purchase.getStatus() == status)
                .filter(purchase -> supplierId == null || purchase.getSupplier().getId().equals(supplierId))
                .filter(purchase -> dateFrom == null || !purchase.getOrderDate().isBefore(dateFrom))
                .filter(purchase -> dateTo == null || !purchase.getOrderDate().isAfter(dateTo))
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse findById(Long id) {
        return mapper.toResponse(getPurchase(id));
    }

    @Override
    public PurchaseResponse create(PurchaseCreateRequest request) {
        validateRequest(request);
        Purchase purchase = buildPurchase(new Purchase(), request);
        Purchase saved = purchaseRepository.save(purchase);
        if (saved.getStatus() == PurchaseStatus.RECEIVED) {
            inventoryService.applyPurchaseStock(saved);
        }
        return mapper.toResponse(saved);
    }

    @Override
    public PurchaseResponse update(Long id, PurchaseUpdateRequest request) {
        validateRequest(request);
        Purchase purchase = getPurchase(id);
        if (Boolean.TRUE.equals(purchase.getStockApplied())) {
            throw new BusinessException("Cannot edit purchase lines after stock has been applied");
        }
        buildPurchase(purchase, request);
        Purchase saved = purchaseRepository.save(purchase);
        if (saved.getStatus() == PurchaseStatus.RECEIVED) {
            inventoryService.applyPurchaseStock(saved);
        }
        return mapper.toResponse(saved);
    }

    @Override
    public PurchaseResponse updateStatus(Long id, PurchaseStatusUpdateRequest request) {
        Purchase purchase = getPurchase(id);
        PurchaseStatus newStatus = request.getStatus();
        if (newStatus == PurchaseStatus.RECEIVED) {
            purchase.setStatus(PurchaseStatus.RECEIVED);
            if (purchase.getActualArrivalDate() == null) {
                purchase.setActualArrivalDate(LocalDateTime.now());
            }
            inventoryService.applyPurchaseStock(purchase);
        } else if (newStatus == PurchaseStatus.CANCELLED && Boolean.TRUE.equals(purchase.getStockApplied())) {
            inventoryService.revertPurchaseStock(purchase);
            purchase.setStatus(PurchaseStatus.CANCELLED);
        } else {
            purchase.setStatus(newStatus);
        }
        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    public void delete(Long id) {
        Purchase purchase = getPurchase(id);
        if (Boolean.TRUE.equals(purchase.getStockApplied())) {
            throw new BusinessException("Cannot delete a received purchase without reverting stock first");
        }
        purchaseRepository.delete(purchase);
    }

    private Purchase buildPurchase(Purchase purchase, PurchaseCreateRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + request.getSupplierId()));
        purchase.setSupplier(supplier);
        purchase.setOrderDate(request.getOrderDate());
        purchase.setEstimatedArrivalDate(request.getEstimatedArrivalDate());
        purchase.setActualArrivalDate(request.getActualArrivalDate());
        purchase.setStatus(request.getStatus() != null ? request.getStatus() : PurchaseStatus.PENDING);
        purchase.setShippingCost(valueOrZero(request.getShippingCost()));
        purchase.setOtherCosts(valueOrZero(request.getOtherCosts()));
        purchase.setTrackingNumber(request.getTrackingNumber());
        purchase.setExternalReference(request.getExternalReference());
        purchase.setNotes(request.getNotes());
        if (purchase.getStockApplied() == null) {
            purchase.setStockApplied(false);
        }
        purchase.clearItems();
        for (PurchaseItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));
            PurchaseItem item = new PurchaseItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPurchasePrice(itemRequest.getUnitPurchasePrice());
            purchase.addItem(item);
        }
        return purchase;
    }

    private void validateRequest(PurchaseCreateRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("Purchase must contain at least one item");
        }
        if (isNegative(request.getShippingCost()) || isNegative(request.getOtherCosts())) {
            throw new BusinessException("Purchase costs cannot be negative");
        }
        for (PurchaseItemRequest item : request.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException("Purchase item quantity must be greater than zero");
            }
            if (item.getUnitPurchasePrice() == null || item.getUnitPurchasePrice().signum() < 0) {
                throw new BusinessException("Purchase item price cannot be negative");
            }
        }
    }

    private Purchase getPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found: " + id));
    }

    private BigDecimal valueOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private boolean isNegative(BigDecimal value) {
        return value != null && value.signum() < 0;
    }
}
