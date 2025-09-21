package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    
    // Базовые методы поиска
    Optional<Unit> findByName(String name);
    Optional<Unit> findByNameAndTenantId(String name, Long tenantId);
    
    // Методы с пагинацией
    Page<Unit> findByTenantId(Long tenantId, Pageable pageable);
    Page<Unit> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId, Pageable pageable);
    
    // Методы для подсчета
    long countByTenantId(Long tenantId);
    long countByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
    
    // Методы проверки существования
    boolean existsByName(String name);
    boolean existsByTenantIdAndName(Long tenantId, String name);
    
    // Старые методы (для обратной совместимости)
    List<Unit> findByTenantId(Long tenantId);
    List<Unit> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
}
