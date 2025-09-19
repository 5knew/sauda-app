package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    List<Inventory> findByTenantId(Integer tenantId);
    
    List<Inventory> findByTenantIdAndWarehouseId(Integer tenantId, Long warehouseId);
    
    List<Inventory> findByTenantIdAndProductId(Integer tenantId, Long productId);
    
    Optional<Inventory> findByTenantIdAndProductIdAndWarehouseId(Integer tenantId, Long productId, Long warehouseId);
    
    @Query("SELECT i FROM Inventory i WHERE i.tenantId = :tenantId AND i.quantity > 0")
    List<Inventory> findAvailableInventory(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT i FROM Inventory i WHERE i.tenantId = :tenantId AND i.quantity <= :threshold")
    List<Inventory> findLowStockInventory(@Param("tenantId") Integer tenantId, @Param("threshold") BigDecimal threshold);
}


