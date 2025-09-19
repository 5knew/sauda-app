package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    
    Optional<Shop> findByTenantId(Integer tenantId);
    
    @Query("SELECT s FROM Shop s WHERE s.tenantId = :tenantId")
    List<Shop> findAllByTenantId(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT s FROM Shop s WHERE s.ownerId = :ownerId")
    List<Shop> findByOwnerId(@Param("ownerId") Long ownerId);
}


