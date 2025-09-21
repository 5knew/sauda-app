package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    
    // Базовые методы поиска
    Optional<Warehouse> findByName(String name);
    Optional<Warehouse> findByNameAndTenantId(String name, Long tenantId);
    
    // Методы с пагинацией
    Page<Warehouse> findByTenantId(Long tenantId, Pageable pageable);
    Page<Warehouse> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId, Pageable pageable);
    
    // Методы для подсчета
    long countByTenantId(Long tenantId);
    long countByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
    
    // Методы проверки существования
    boolean existsByName(String name);
    boolean existsByTenantIdAndName(Long tenantId, String name);
    
    // Старые методы (для обратной совместимости)
    List<Warehouse> findByTenantId(Long tenantId);
    List<Warehouse> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
}
