package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Базовые методы поиска
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByDiscountCard(String discountCard);
    Optional<Customer> findByDiscountCardAndTenantId(String discountCard, Long tenantId);
    
    // Методы с пагинацией
    Page<Customer> findByTenantId(Long tenantId, Pageable pageable);
    Page<Customer> findByFullNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Customer> findByTenantIdAndFullNameContainingIgnoreCase(Long tenantId, String name, Pageable pageable);
    Page<Customer> findByPhoneContaining(String phone, Pageable pageable);
    Page<Customer> findByTenantIdAndPhoneContaining(Long tenantId, String phone, Pageable pageable);
    Page<Customer> findByTenantIdAndDiscountCardIsNotNull(Long tenantId, Pageable pageable);
    Page<Customer> findByTenantIdAndDiscountCardIsNull(Long tenantId, Pageable pageable);
    
    // Методы для подсчета
    long countByTenantId(Long tenantId);
    long countByTenantIdAndDiscountCardIsNotNull(Long tenantId);
    
    // Методы проверки существования
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByDiscountCard(String discountCard);
    boolean existsByTenantIdAndPhone(Long tenantId, String phone);
    boolean existsByTenantIdAndEmail(Long tenantId, String email);
    boolean existsByTenantIdAndDiscountCard(Long tenantId, String discountCard);
    
    // Старые методы (для обратной совместимости)
    List<Customer> findByTenantId(Long tenantId);
    
    @Query("SELECT c FROM Customer c WHERE c.tenantId = :tenantId AND " +
           "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.discountCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Customer> searchCustomers(@Param("tenantId") Long tenantId, @Param("searchTerm") String searchTerm);
}


