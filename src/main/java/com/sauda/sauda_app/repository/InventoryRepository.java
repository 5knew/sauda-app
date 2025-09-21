package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    // Базовые методы поиска
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<Inventory> findByProductId(Long productId);
    Page<Inventory> findByProductId(Long productId, Pageable pageable);
    
    // Методы с пагинацией
    Page<Inventory> findByWarehouseId(Long warehouseId, Pageable pageable);
    Page<Inventory> findByTenantId(Long tenantId, Pageable pageable);
    List<Inventory> findByTenantIdAndProductId(Long tenantId, Long productId);
    
    // Методы для фильтрации по количеству
    Page<Inventory> findByTenantIdAndQuantity(Long tenantId, BigDecimal quantity, Pageable pageable);
    Page<Inventory> findByTenantIdAndQuantityLessThan(Long tenantId, BigDecimal threshold, Pageable pageable);
    Page<Inventory> findByTenantIdAndQuantityGreaterThan(Long tenantId, BigDecimal threshold, Pageable pageable);
    
    // Методы для подсчета
    long countDistinctProductIdByWarehouseId(Long warehouseId);
    long countDistinctProductIdByTenantId(Long tenantId);
    
    // Методы проверки существования
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
    
    // Старые методы (для обратной совместимости)
    List<Inventory> findByTenantId(Long tenantId);
    List<Inventory> findByTenantIdAndWarehouseId(Long tenantId, Long warehouseId);
    Optional<Inventory> findByTenantIdAndProductIdAndWarehouseId(Long tenantId, Long productId, Long warehouseId);
    
    @Query("SELECT i FROM Inventory i WHERE i.tenantId = :tenantId AND i.quantity > 0")
    List<Inventory> findAvailableInventory(@Param("tenantId") Long tenantId);
    
    @Query("SELECT i FROM Inventory i WHERE i.tenantId = :tenantId AND i.quantity <= :threshold")
    List<Inventory> findLowStockInventory(@Param("tenantId") Long tenantId, @Param("threshold") BigDecimal threshold);
}


