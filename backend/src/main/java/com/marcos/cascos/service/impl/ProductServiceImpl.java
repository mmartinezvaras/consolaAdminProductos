package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.ProductCreateRequest;
import com.marcos.cascos.dto.ProductResponse;
import com.marcos.cascos.dto.ProductUpdateRequest;
import com.marcos.cascos.entity.Product;
import com.marcos.cascos.entity.ProductModel;
import com.marcos.cascos.entity.Supplier;
import com.marcos.cascos.exception.BusinessException;
import com.marcos.cascos.exception.ConflictException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.ProductMapper;
import com.marcos.cascos.repository.ProductModelRepository;
import com.marcos.cascos.repository.ProductRepository;
import com.marcos.cascos.repository.SupplierRepository;
import com.marcos.cascos.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductModelRepository productModelRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductModelRepository productModelRepository, SupplierRepository supplierRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.productModelRepository = productModelRepository;
        this.supplierRepository = supplierRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findActive() {
        return productRepository.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findLowStock() {
        return productRepository.findLowStockProducts().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public ProductResponse create(ProductCreateRequest request) {
        validateBusinessRules(request);
        validateSerialNumber(request.getSerialNumber(), null);
        ProductModel model = getModel(request);
        Supplier supplier = getSupplier(request.getSupplierId());
        return mapper.toResponse(productRepository.save(mapper.toEntity(request, model, supplier)));
    }

    @Override
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        validateBusinessRules(request);
        validateSerialNumber(request.getSerialNumber(), id);
        Product product = getEntity(id);
        ProductModel model = getModel(request);
        Supplier supplier = getSupplier(request.getSupplierId());
        mapper.updateEntity(product, request, model, supplier);
        return mapper.toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = getEntity(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private ProductModel getModel(ProductCreateRequest request) {
        if (request.getProductModelId() != null) {
            return productModelRepository.findById(request.getProductModelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product model not found: " + request.getProductModelId()));
        }
        String modelName = request.getProductModelName();
        if (modelName == null || modelName.isBlank()) {
            throw new BusinessException("Product model is required");
        }
        return productModelRepository.findByNameIgnoreCaseAndActiveTrue(modelName.trim())
                .orElseGet(() -> {
                    ProductModel model = new ProductModel();
                    model.setName(modelName.trim());
                    model.setActive(true);
                    return productModelRepository.save(model);
                });
    }

    private Supplier getSupplier(Long id) {
        if (id == null) {
            return null;
        }
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }

    private void validateBusinessRules(ProductCreateRequest request) {
        assertNonNegative(request.getUsualPurchasePrice(), "usualPurchasePrice cannot be negative");
        assertNonNegative(request.getPurchaseShippingCost(), "purchaseShippingCost cannot be negative");
        assertNonNegative(request.getOtherPurchaseCosts(), "otherPurchaseCosts cannot be negative");
        assertNonNegative(request.getRecommendedSalePrice(), "recommendedSalePrice cannot be negative");
        int currentStock = valueOrZero(request.getCurrentStock());
        int reservedStock = valueOrZero(request.getReservedStock());
        int minimumStock = valueOrZero(request.getMinimumStock());
        if (currentStock < 0 || reservedStock < 0 || minimumStock < 0) {
            throw new BusinessException("Stock values cannot be negative");
        }
        if (reservedStock > currentStock) {
            throw new BusinessException("Reserved stock cannot be greater than current stock");
        }
    }

    private void validateSerialNumber(String serialNumber, Long currentId) {
        if (serialNumber == null || serialNumber.isBlank()) {
            return;
        }
        boolean exists = currentId == null
                ? productRepository.existsBySerialNumberIgnoreCase(serialNumber)
                : productRepository.existsBySerialNumberIgnoreCaseAndIdNot(serialNumber, currentId);
        if (exists) {
            throw new ConflictException("Serial number already exists");
        }
    }

    private void assertNonNegative(BigDecimal value, String message) {
        if (value != null && value.signum() < 0) {
            throw new BusinessException(message);
        }
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }
}
