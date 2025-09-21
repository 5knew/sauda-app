package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Базовые методы поиска
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findBySku(String sku);
    Optional<Product> findByBarcodeAndTenantId(String barcode, Long tenantId);
    Optional<Product> findBySkuAndTenantId(String sku, Long tenantId);
    
    // Методы с пагинацией
    Page<Product> findByTenantId(Long tenantId, Pageable pageable);
    Page<Product> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);
    Page<Product> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId, Pageable pageable);
    Page<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);
    Page<Product> findByTenantIdAndCategoryIdAndIsDeletedFalse(Long tenantId, Long categoryId, Pageable pageable);
    
    // Поиск по названию
    Page<Product> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    Page<Product> findByTenantIdAndNameContainingIgnoreCaseAndIsDeletedFalse(Long tenantId, String name, Pageable pageable);
    
    // Методы для подсчета
    long countByTenantId(Long tenantId);
    long countByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
    
    // Методы проверки существования
    boolean existsByBarcode(String barcode);
    boolean existsBySku(String sku);
    boolean existsByTenantIdAndBarcode(Long tenantId, String barcode);
    boolean existsByTenantIdAndSku(Long tenantId, String sku);
    
    // Старые методы (для обратной совместимости)
    List<Product> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
    List<Product> findByTenantIdAndCategoryIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId, Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isActive = true AND p.isDeleted = false")
    List<Product> searchProducts(@Param("tenantId") Long tenantId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND p.isActive = true AND p.isDeleted = false " +
           "ORDER BY p.name")
    List<Product> findAllActiveByTenantIdOrderByName(@Param("tenantId") Long tenantId);
}


