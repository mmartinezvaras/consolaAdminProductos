package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.InventoryAdjustmentRequest;
import com.marcos.cascos.dto.InventoryMovementResponse;
import com.marcos.cascos.dto.InventorySummaryResponse;
import com.marcos.cascos.entity.InventoryMovement;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.Purchase;
import com.marcos.cascos.entity.PurchaseItem;
import com.marcos.cascos.enums.InventoryMovementType;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.InventoryMovementMapper;
import com.marcos.cascos.mapper.InventorySummaryMapper;
import com.marcos.cascos.repository.InventoryMovementRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.PurchaseItemRepository;
import com.marcos.cascos.service.InventoryService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final InventoryMovementRepository movementRepository;
    private final InventoryMovementMapper movementMapper;
    private final InventorySummaryMapper summaryMapper;

    public InventoryServiceImpl(
            ProductRepository productRepository,
            PurchaseItemRepository purchaseItemRepository,
            InventoryMovementRepository movementRepository,
            InventoryMovementMapper movementMapper,
            InventorySummaryMapper summaryMapper) {
        this.productRepository = productRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.movementRepository = movementRepository;
        this.movementMapper = movementMapper;
        this.summaryMapper = summaryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventorySummaryResponse> getInventory() {
        return productRepository.findAll().stream()
                .map(product -> summaryMapper.toResponse(product, calculateAveragePurchasePrice(product)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> getMovements() {
        return movementRepository.findAll().stream().map(movementMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> getMovementsByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found: " + productId);
        }
        return movementRepository.findByProductIdOrderByMovementDateDesc(productId).stream()
                .map(movementMapper::toResponse)
                .toList();
    }

    @Override
    public InventoryMovementResponse adjustStock(InventoryAdjustmentRequest request) {
        if (request.getQuantity() == null || request.getQuantity() == 0) {
            throw new BusinessException("Adjustment quantity cannot be zero");
        }
        Product product = getProductForUpdate(request.getProductId());
        int previousStock = safeStock(product);
        int resultingStock = previousStock + request.getQuantity();
        if (resultingStock < 0) {
            throw new BusinessException("Adjustment would leave negative stock");
        }
        product.setCurrentStock(resultingStock);
        productRepository.save(product);
        InventoryMovement movement = createMovement(
                product,
                null,
                request.getQuantity() > 0 ? InventoryMovementType.MANUAL_IN : InventoryMovementType.MANUAL_OUT,
                Math.abs(request.getQuantity()),
                previousStock,
                resultingStock,
                request.getReason(),
                request.getNotes(),
                request.getMovementDate());
        return movementMapper.toResponse(movementRepository.save(movement));
    }

    @Override
    public void applyPurchaseStock(Purchase purchase) {
        if (Boolean.TRUE.equals(purchase.getStockApplied())) {
            throw new BusinessException("Purchase stock has already been applied");
        }
        if (purchase.getItems() == null || purchase.getItems().isEmpty()) {
            throw new BusinessException("Cannot apply stock for a purchase without items");
        }
        for (PurchaseItem item : purchase.getItems()) {
            Product product = getProductForUpdate(item.getProduct().getId());
            int previousStock = safeStock(product);
            int resultingStock = previousStock + item.getQuantity();
            product.setCurrentStock(resultingStock);
            productRepository.save(product);
            movementRepository.save(createMovement(
                    product,
                    purchase,
                    InventoryMovementType.PURCHASE,
                    item.getQuantity(),
                    previousStock,
                    resultingStock,
                    "Purchase received",
                    null,
                    purchase.getActualArrivalDate()));
        }
        if (purchase.getActualArrivalDate() == null) {
            purchase.setActualArrivalDate(LocalDateTime.now());
        }
        purchase.setStockApplied(true);
    }

    @Override
    public void revertPurchaseStock(Purchase purchase) {
        if (!Boolean.TRUE.equals(purchase.getStockApplied())) {
            throw new BusinessException("Purchase stock is not applied");
        }
        for (PurchaseItem item : purchase.getItems()) {
            Product product = getProductForUpdate(item.getProduct().getId());
            int previousStock = safeStock(product);
            int resultingStock = previousStock - item.getQuantity();
            if (resultingStock < 0) {
                throw new BusinessException("Not enough stock to revert purchase");
            }
            product.setCurrentStock(resultingStock);
            productRepository.save(product);
            movementRepository.save(createMovement(
                    product,
                    purchase,
                    InventoryMovementType.PURCHASE_REVERSAL,
                    item.getQuantity(),
                    previousStock,
                    resultingStock,
                    "Purchase stock reverted",
                    null,
                    LocalDateTime.now()));
        }
        purchase.setStockApplied(false);
    }

    private Product getProductForUpdate(Long productId) {
        return productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    private InventoryMovement createMovement(
            Product product,
            Purchase purchase,
            InventoryMovementType type,
            Integer quantity,
            Integer previousStock,
            Integer resultingStock,
            String reason,
            String notes,
            LocalDateTime movementDate) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setPurchase(purchase);
        movement.setMovementType(type);
        movement.setQuantity(quantity);
        movement.setPreviousStock(previousStock);
        movement.setResultingStock(resultingStock);
        movement.setReason(reason);
        movement.setNotes(notes);
        movement.setMovementDate(movementDate != null ? movementDate : LocalDateTime.now());
        return movement;
    }

    private BigDecimal calculateAveragePurchasePrice(Product product) {
        List<PurchaseItem> receivedItems = purchaseItemRepository.findReceivedItemsByProductId(product.getId());
        int totalQuantity = receivedItems.stream().mapToInt(PurchaseItem::getQuantity).sum();
        if (totalQuantity == 0) {
            // Fallback: when there are no received purchases, inventory is valued with the product's usual purchase price.
            return product.getUsualPurchasePrice() != null ? product.getUsualPurchasePrice() : BigDecimal.ZERO;
        }
        BigDecimal totalCost = receivedItems.stream()
                .map(item -> item.getUnitPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalCost.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
    }

    private int safeStock(Product product) {
        return product.getCurrentStock() == null ? 0 : product.getCurrentStock();
    }
}
