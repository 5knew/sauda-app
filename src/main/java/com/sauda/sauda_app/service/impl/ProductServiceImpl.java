package com.sauda.sauda_app.service.impl;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.entity.ProductCategory;
import com.sauda.sauda_app.entity.Unit;
import com.sauda.sauda_app.repository.ProductRepository;
import com.sauda.sauda_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация сервиса для управления товарами
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        // Валидация входных данных
        validateProductDto(productDto);

        // Проверка уникальности штрих-кода
        if (productRepository.existsByBarcode(productDto.getBarcode())) {
            throw new IllegalArgumentException("Товар с штрих-кодом " + productDto.getBarcode() + " уже существует");
        }

        // Проверка уникальности артикула
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new IllegalArgumentException("Товар с артикулом " + productDto.getSku() + " уже существует");
        }

        // Создание нового товара
        Product product = new Product();
        product.setTenantId(productDto.getTenantId());
        product.setName(productDto.getName());
        product.setBarcode(productDto.getBarcode());
        product.setSku(productDto.getSku());
        product.setDescription(productDto.getDescription());
        product.setIsActive(true);
        product.setIsDeleted(false);

        // Установка категории если указана
        if (productDto.getCategoryId() != null) {
            ProductCategory category = new ProductCategory();
            category.setId(productDto.getCategoryId());
            product.setCategory(category);
        }

        // Установка единицы измерения если указана
        if (productDto.getUnitId() != null) {
            Unit unit = new Unit();
            unit.setId(productDto.getUnitId());
            product.setUnit(unit);
        }

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) {
        // Валидация входных данных
        validateProductDto(productDto);

        // Поиск существующего товара
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));

        // Проверка уникальности штрих-кода (если изменился)
        if (!existingProduct.getBarcode().equals(productDto.getBarcode()) &&
            productRepository.existsByBarcode(productDto.getBarcode())) {
            throw new IllegalArgumentException("Товар с штрих-кодом " + productDto.getBarcode() + " уже существует");
        }

        // Проверка уникальности артикула (если изменился)
        if (!existingProduct.getSku().equals(productDto.getSku()) &&
            productRepository.existsBySku(productDto.getSku())) {
            throw new IllegalArgumentException("Товар с артикулом " + productDto.getSku() + " уже существует");
        }

        // Обновление данных товара
        existingProduct.setName(productDto.getName());
        existingProduct.setBarcode(productDto.getBarcode());
        existingProduct.setSku(productDto.getSku());
        existingProduct.setDescription(productDto.getDescription());

        // Обновление категории если указана
        if (productDto.getCategoryId() != null) {
            ProductCategory category = new ProductCategory();
            category.setId(productDto.getCategoryId());
            existingProduct.setCategory(category);
        } else {
            existingProduct.setCategory(null);
        }

        // Обновление единицы измерения если указана
        if (productDto.getUnitId() != null) {
            Unit unit = new Unit();
            unit.setId(productDto.getUnitId());
            existingProduct.setUnit(unit);
        } else {
            existingProduct.setUnit(null);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByTenant(Long tenantId, Pageable pageable) {
        return productRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrueAndIsDeletedFalse(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getActiveProductsByTenant(Long tenantId, Pageable pageable) {
        return productRepository.findByTenantIdAndIsActiveTrueAndIsDeletedFalse(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProductsByNameInTenant(Long tenantId, String name, Pageable pageable) {
        return productRepository.findByTenantIdAndNameContainingIgnoreCaseAndIsDeletedFalse(tenantId, name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategoryInTenant(Long tenantId, Long categoryId, Pageable pageable) {
        return productRepository.findByTenantIdAndCategoryIdAndIsDeletedFalse(tenantId, categoryId, pageable);
    }

    @Override
    public Product activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));
        
        product.setIsActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));
        
        product.setIsActive(false);
        return productRepository.save(product);
    }

    @Override
    public void softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));
        
        product.setIsDeleted(true);
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    public Product restoreProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));
        
        product.setIsDeleted(false);
        product.setIsActive(true);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCountByTenant(Long tenantId) {
        return productRepository.countByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveProductCountByTenant(Long tenantId) {
        return productRepository.countByTenantIdAndIsActiveTrueAndIsDeletedFalse(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBarcodeInTenant(Long tenantId, String barcode) {
        return productRepository.existsByTenantIdAndBarcode(tenantId, barcode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySkuInTenant(Long tenantId, String sku) {
        return productRepository.existsByTenantIdAndSku(tenantId, sku);
    }

    /**
     * Валидация DTO товара
     * @param productDto DTO товара
     */
    private void validateProductDto(ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("DTO товара не может быть null");
        }
        if (productDto.getTenantId() == null) {
            throw new IllegalArgumentException("ID магазина обязателен");
        }
        if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название товара обязательно");
        }
        if (productDto.getBarcode() == null || productDto.getBarcode().trim().isEmpty()) {
            throw new IllegalArgumentException("Штрих-код товара обязателен");
        }
        if (productDto.getSku() == null || productDto.getSku().trim().isEmpty()) {
            throw new IllegalArgumentException("Артикул товара обязателен");
        }
    }
}
