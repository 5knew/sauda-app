package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Integer tenantId);
    
    List<Product> findByTenantIdAndCategoryIdAndIsActiveTrueAndIsDeletedFalse(Integer tenantId, Long categoryId);
    
    Optional<Product> findByBarcodeAndTenantId(String barcode, Integer tenantId);
    
    Optional<Product> findBySkuAndTenantId(String sku, Integer tenantId);
    
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isActive = true AND p.isDeleted = false")
    List<Product> searchProducts(@Param("tenantId") Integer tenantId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND p.isActive = true AND p.isDeleted = false " +
           "ORDER BY p.name")
    List<Product> findAllActiveByTenantIdOrderByName(@Param("tenantId") Integer tenantId);
}


