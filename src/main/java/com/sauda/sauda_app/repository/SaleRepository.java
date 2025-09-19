package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findByTenantId(Integer tenantId);
    
    List<Sale> findByTenantIdAndEmployeeId(Integer tenantId, Long employeeId);
    
    List<Sale> findByTenantIdAndCustomerId(Integer tenantId, Long customerId);
    
    List<Sale> findByTenantIdAndPosTerminalId(Integer tenantId, Long posTerminalId);
    
    @Query("SELECT s FROM Sale s WHERE s.tenantId = :tenantId AND s.saleDate BETWEEN :startDate AND :endDate")
    List<Sale> findByTenantIdAndSaleDateBetween(@Param("tenantId") Integer tenantId, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.tenantId = :tenantId ORDER BY s.saleDate DESC")
    List<Sale> findByTenantIdOrderBySaleDateDesc(@Param("tenantId") Integer tenantId);
}


