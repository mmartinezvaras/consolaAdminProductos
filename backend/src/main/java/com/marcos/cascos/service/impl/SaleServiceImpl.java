package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.SaleCreateRequest;
import com.marcos.cascos.dto.SaleItemRequest;
import com.marcos.cascos.dto.SaleResponse;
import com.marcos.cascos.dto.SaleStatusUpdateRequest;
import com.marcos.cascos.dto.SaleUpdateRequest;
import com.marcos.cascos.entity.InventoryMovement;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.Sale;
import com.marcos.cascos.entity.SaleItem;
import com.marcos.cascos.enums.InventoryMovementType;
import com.marcos.cascos.enums.PaymentMethod;
import com.marcos.cascos.enums.SalePlatform;
import com.marcos.cascos.enums.SaleStatus;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.SaleMapper;
import com.marcos.cascos.repository.InventoryMovementRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.SaleRepository;
import com.marcos.cascos.service.SaleService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;
    private final SaleMapper mapper;

    public SaleServiceImpl(SaleRepository saleRepository, ProductRepository productRepository, InventoryMovementRepository movementRepository, SaleMapper mapper) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findAll(SaleStatus status, SalePlatform platform, PaymentMethod paymentMethod, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return saleRepository.findAll().stream()
                .filter(s -> status == null || s.getStatus() == status)
                .filter(s -> platform == null || s.getPlatform() == platform)
                .filter(s -> paymentMethod == null || s.getPaymentMethod() == paymentMethod)
                .filter(s -> dateFrom == null || !s.getSaleDate().isBefore(dateFrom))
                .filter(s -> dateTo == null || !s.getSaleDate().isAfter(dateTo))
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long id) {
        return mapper.toResponse(getSale(id));
    }

    @Override
    public SaleResponse create(SaleCreateRequest request) {
        validate(request);
        Sale sale = buildSale(new Sale(), request);
        Sale saved = saleRepository.save(sale);
        if (saved.getStatus() == SaleStatus.COMPLETED) {
            applySaleStock(saved);
        }
        return mapper.toResponse(saved);
    }

    @Override
    public SaleResponse update(Long id, SaleUpdateRequest request) {
        validate(request);
        Sale sale = getSale(id);
        if (Boolean.TRUE.equals(sale.getStockApplied())) {
            throw new BusinessException("Cannot edit a completed sale while stock is applied");
        }
        buildSale(sale, request);
        Sale saved = saleRepository.save(sale);
        if (saved.getStatus() == SaleStatus.COMPLETED) {
            applySaleStock(saved);
        }
        return mapper.toResponse(saved);
    }

    @Override
    public SaleResponse updateStatus(Long id, SaleStatusUpdateRequest request) {
        Sale sale = getSale(id);
        if (request.getStatus() == SaleStatus.COMPLETED) {
            sale.setStatus(SaleStatus.COMPLETED);
            applySaleStock(sale);
        } else if ((request.getStatus() == SaleStatus.CANCELLED || request.getStatus() == SaleStatus.REFUNDED) && Boolean.TRUE.equals(sale.getStockApplied())) {
            revertSaleStock(sale);
            sale.setStatus(request.getStatus());
        } else {
            sale.setStatus(request.getStatus());
        }
        return mapper.toResponse(saleRepository.save(sale));
    }

    @Override
    public void delete(Long id) {
        Sale sale = getSale(id);
        if (Boolean.TRUE.equals(sale.getStockApplied())) {
            throw new BusinessException("Cannot delete a sale while stock is applied");
        }
        saleRepository.delete(sale);
    }

    private Sale buildSale(Sale sale, SaleCreateRequest request) {
        sale.setBuyerReference(request.getBuyerReference());
        sale.setSaleDate(request.getSaleDate());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setPlatform(request.getPlatform());
        sale.setStatus(request.getStatus() != null ? request.getStatus() : SaleStatus.PENDING);
        sale.setCommission(value(request.getCommission()));
        sale.setShippingCost(value(request.getShippingCost()));
        sale.setOtherCosts(value(request.getOtherCosts()));
        sale.setNotes(request.getNotes());
        if (sale.getStockApplied() == null) {
            sale.setStockApplied(false);
        }
        sale.clearItems();
        for (SaleItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));
            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitSalePrice(itemRequest.getUnitSalePrice());
            item.setUnitPurchaseCost(product.getUsualPurchasePrice() != null ? product.getUsualPurchasePrice() : BigDecimal.ZERO);
            sale.addItem(item);
        }
        return sale;
    }

    private void applySaleStock(Sale sale) {
        if (Boolean.TRUE.equals(sale.getStockApplied())) {
            throw new BusinessException("Sale stock has already been applied");
        }
        for (SaleItem item : sale.getItems()) {
            Product product = getProductForUpdate(item.getProduct().getId());
            int current = product.getCurrentStock() == null ? 0 : product.getCurrentStock();
            int reserved = product.getReservedStock() == null ? 0 : product.getReservedStock();
            int available = current - reserved;
            if (item.getQuantity() > available) {
                throw new BusinessException("Not enough stock for product " + product.getId());
            }
            int resulting = current - item.getQuantity();
            product.setCurrentStock(resulting);
            productRepository.save(product);
            movementRepository.save(movement(product, sale, InventoryMovementType.SALE, item.getQuantity(), current, resulting, "Sale completed"));
        }
        sale.setStockApplied(true);
    }

    private void revertSaleStock(Sale sale) {
        if (!Boolean.TRUE.equals(sale.getStockApplied())) {
            throw new BusinessException("Sale stock is not applied");
        }
        for (SaleItem item : sale.getItems()) {
            Product product = getProductForUpdate(item.getProduct().getId());
            int current = product.getCurrentStock() == null ? 0 : product.getCurrentStock();
            int resulting = current + item.getQuantity();
            product.setCurrentStock(resulting);
            productRepository.save(product);
            movementRepository.save(movement(product, sale, InventoryMovementType.SALE_REVERSAL, item.getQuantity(), current, resulting, "Sale reverted"));
        }
        sale.setStockApplied(false);
    }

    private InventoryMovement movement(Product product, Sale sale, InventoryMovementType type, int quantity, int previous, int resulting, String reason) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setSale(sale);
        movement.setMovementType(type);
        movement.setQuantity(quantity);
        movement.setPreviousStock(previous);
        movement.setResultingStock(resulting);
        movement.setReason(reason);
        movement.setMovementDate(LocalDateTime.now());
        return movement;
    }

    private void validate(SaleCreateRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("Sale must contain at least one item");
        }
        if (isNegative(request.getCommission()) || isNegative(request.getShippingCost()) || isNegative(request.getOtherCosts())) {
            throw new BusinessException("Sale costs cannot be negative");
        }
        for (SaleItemRequest item : request.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException("Sale item quantity must be greater than zero");
            }
            if (item.getUnitSalePrice() == null || item.getUnitSalePrice().signum() < 0) {
                throw new BusinessException("Sale item price cannot be negative");
            }
        }
    }

    private Product getProductForUpdate(Long productId) {
        return productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    private Sale getSale(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + id));
    }

    private BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private boolean isNegative(BigDecimal value) {
        return value != null && value.signum() < 0;
    }
}
