package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Базовые методы поиска
    Optional<Category> findByName(String name);
    Optional<Category> findByNameAndTenantId(String name, Long tenantId);
    
    // Методы с пагинацией
    Page<Category> findByTenantId(Long tenantId, Pageable pageable);
    Page<Category> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId, Pageable pageable);
    
    // Методы для подсчета
    long countByTenantId(Long tenantId);
    long countByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
    
    // Методы проверки существования
    boolean existsByName(String name);
    boolean existsByTenantIdAndName(Long tenantId, String name);
    
    // Старые методы (для обратной совместимости)
    List<Category> findByTenantId(Long tenantId);
    List<Category> findByTenantIdAndIsActiveTrueAndIsDeletedFalse(Long tenantId);
}
