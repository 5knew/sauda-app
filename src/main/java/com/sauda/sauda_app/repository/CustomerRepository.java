package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByTenantId(Integer tenantId);
    
    Optional<Customer> findByDiscountCardAndTenantId(String discountCard, Integer tenantId);
    
    @Query("SELECT c FROM Customer c WHERE c.tenantId = :tenantId AND " +
           "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.discountCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Customer> searchCustomers(@Param("tenantId") Integer tenantId, @Param("searchTerm") String searchTerm);
}


